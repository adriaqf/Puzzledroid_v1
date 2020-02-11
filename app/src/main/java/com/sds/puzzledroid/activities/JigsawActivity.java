package com.sds.puzzledroid.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.sds.puzzledroid.logic.Jigsaw;
import com.sds.puzzledroid.logic.PuzzlePiece;
import com.sds.puzzledroid.R;
import com.sds.puzzledroid.TouchListener;
import com.sds.puzzledroid.logic.Score;
import com.sds.puzzledroid.sqlite.SQLiteScore;

import java.util.Collections;
import java.util.Random;

public class JigsawActivity extends AppCompatActivity {
    private Jigsaw jigsaw;
    private Chronometer chronometer;
    private int localDifficulty;
    ImageView ivPuzzle;
    ImageView ivwin;
    Animation animZoomInBlind, zoomOutIn;
    Animation alpha;

    SoundPool sp;
    int succefullSound;
    int clickSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        succefullSound = sp.load(this, R.raw.succefull, 1);
        clickSound = sp.load(this, R.raw.clic, 1);

        chronometer = findViewById(R.id.chrono);

        final RelativeLayout layout = findViewById(R.id.rLayoutPuzzleLevel);
        final ImageView imageView = findViewById(R.id.ivPuzzle);
        ivPuzzle = findViewById(R.id.ivPuzzle); // Alpha background (puzzle)
        ivwin = findViewById(R.id.ivwin); // Victory stars (for animation)

        animZoomInBlind = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_in_blind); // Parpadeo de stars
        alpha = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.keep_in_alpha_0); // Puts stars in alpha 0
        zoomOutIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_in); // Zoom out + in

        ivwin.startAnimation(alpha); // Hides stars picture

        Intent intent = getIntent();
        final String assetName = intent.getStringExtra("assetName");
        this.localDifficulty = intent.getIntExtra("levelDifficulty", 1);

        imageView.post(new Runnable() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void run() {
                jigsaw = new Jigsaw(getApplicationContext(), assetName, imageView, localDifficulty);
                TouchListener touchListener = new TouchListener(JigsawActivity.this);
                //Shuffle pieces order
                Collections.shuffle(jigsaw.getPieces());
                for (PuzzlePiece piece : jigsaw.getPieces()) {
                    piece.setOnTouchListener(touchListener);
                    layout.addView(piece);
                    // randomize position, on the bottom of the screen
                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                    lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                    lParams.topMargin = layout.getHeight() - piece.pieceHeight - 50;
                    piece.setLayoutParams(lParams);
                }
            }
        });

        chronometer.start();
    }

    public void onClickCloseJS(View view) {
        soundPoolBtn();
        finish();
    }

    // Winning sound
    public void soundPoolJigsawComplete() {
        SharedPreferences pref = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("effects_sound", true);
        if (value) {
            sp.play(succefullSound, 1, 1, 1, 0, 0);
        }
    }

    public void soundPoolBtn() {
        SharedPreferences pref = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("effects_sound", true);

        if (value) {
            sp.play(clickSound, 1, 1, 1, 0, 0);
        }
    }


    public void checkGameOver() {
        if (jigsaw.isJigsawCompleted()) {

            //Hide puzzle animation
            ivPuzzle.setAlpha(1f);
            ivPuzzle.bringToFront();
            ivPuzzle.startAnimation(zoomOutIn);
            ivwin.startAnimation(animZoomInBlind);
            //Completed puzzle sound
            soundPoolJigsawComplete();

            chronometer.stop();

            int totalScore = getChronometerSeconds();
            Score score = new Score(totalScore, localDifficulty);

            //Inserting the new score on the db
            SQLiteScore sqLiteScore = new SQLiteScore(this, score);
            sqLiteScore.insertScore();


            final Intent iPopUp = new Intent(this, PopupCustomActivity.class);
            iPopUp.putExtra("totalScore", totalScore);
            iPopUp.putExtra("difficulty", localDifficulty);

            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(2800);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ivwin.startAnimation(alpha);
                    }
                }
            };
            timer.start();
            startActivity(iPopUp);
        }

    }

    public int getChronometerSeconds() {
        int pos0, pos1, pos3, pos4, total;
        String sChronometer;
        CharSequence timeSecs;

        timeSecs = chronometer.getText();
        sChronometer = timeSecs.toString();
        pos0 = Integer.parseInt(String.valueOf(sChronometer.charAt(0))) * 10 * 60;
        pos1 = Integer.parseInt(String.valueOf(sChronometer.charAt(1))) * 60;
        pos3 = Integer.parseInt(String.valueOf(sChronometer.charAt(3))) * 10;
        pos4 = Integer.parseInt(String.valueOf(sChronometer.charAt(4)));

        total = pos0 + pos1 + pos3 + pos4;

        return total;
    }

}

