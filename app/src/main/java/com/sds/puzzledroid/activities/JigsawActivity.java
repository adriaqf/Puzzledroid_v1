package com.sds.puzzledroid.activities;

import android.annotation.SuppressLint;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;

import android.app.Notification;
import android.app.PendingIntent;

import android.content.Intent;

import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sds.puzzledroid.pojos.ImagePuzzle;
import com.sds.puzzledroid.pojos.Jigsaw;
import com.sds.puzzledroid.pojos.LocalCalendar;
import com.sds.puzzledroid.pojos.PuzzlePiece;
import com.sds.puzzledroid.R;

import com.sds.puzzledroid.pojos.TouchListener;
import com.sds.puzzledroid.pojos.Score;
import com.sds.puzzledroid.sqlite.SQLiteGalleryPhoto;
import com.sds.puzzledroid.sqlite.SQLiteScore;

import java.util.Collections;
import java.util.Locale;
import java.util.Random;


import static com.sds.puzzledroid.activities.AppNotChannels.CHANNEL_1_ID;

public class JigsawActivity extends AppCompatActivity {
    private Jigsaw jigsaw;
    private Chronometer chronometer;
    private int totalScore;
    private int localDifficulty;
    private Uri uImagePath;

    ImageView ivPuzzle;
    ImageView ivwin;
    Animation animZoomInBlind, zoomOutIn;
    Animation alpha;

    SoundPool sp;
    int succefullSound;
    int clickSound;

    private NotificationManagerCompat managerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw);

        managerCompat = NotificationManagerCompat.from(this);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        succefullSound = sp.load(this, R.raw.succefull, 1);
        clickSound = sp.load(this, R.raw.clic, 1);

        final RelativeLayout layout = findViewById(R.id.rLayoutPuzzleLevel);
        final ImageView imageView = findViewById(R.id.ivPuzzle);
        chronometer = findViewById(R.id.chrono);

        // Gets randomized image
        ImagePuzzle imagePuzzle = new ImagePuzzle(this, imageView);
        uImagePath = imagePuzzle.randomizeJigsawImage();

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
        this.localDifficulty = intent.getIntExtra("levelDifficulty", 1);

        imageView.post(new Runnable() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void run() {
                jigsaw = new Jigsaw(getApplicationContext(), imageView, localDifficulty);
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

    private void sendNotificationCGameOver() {
        Intent fullScreenIntent = new Intent(this, Jigsaw.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        System.out.println("SCORE: " + totalScore);
        Intent intent = new Intent(this, PopupCustomActivity.class);
        intent.putExtra("totalScore", totalScore);
        intent.putExtra("difficulty", localDifficulty);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_notification_mood_24dp)
                .setContentTitle(getString(R.string.completado) )
                .setContentText(getString(R.string.verescore))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setTimeoutAfter(5000)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .build();
        managerCompat.notify(1, notification);

       /* String L8= Locale.getDefault().toString();

        switch(L8)
        {
            case "en_US":
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                 .setSmallIcon(R.drawable.ic_notification_mood_24dp)
                    .setContentTitle("¡COMPLETED!")
                    .setContentText("Click here to view your score.")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setTimeoutAfter(5000)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .build();
                     managerCompat.notify(1, notification);
                     break;

            case "fr_FR":
                Notification notification2 = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                 .setSmallIcon(R.drawable.ic_notification_mood_24dp)
                    .setContentTitle("¡COMPLÉTÉ!")
                    .setContentText("Cliquez ici pour voir votre score.")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setTimeoutAfter(5000)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .build();
                     managerCompat.notify(1, notification2);
                     break;
            default:
                Notification notification3 = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_notification_mood_24dp)
                    .setContentTitle("¡COMPLETADO!")
                    .setContentText("Haz click aquí para visualizar tu puntuación.")
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setTimeoutAfter(5000)
                    .setFullScreenIntent(fullScreenPendingIntent, true)
                    .build();
                     managerCompat.notify(1, notification3);
        }
*/
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
            totalScore = getChronometerSeconds();

            sendNotificationCGameOver();

            Score score = new Score(totalScore, localDifficulty);

            //Inserting the new score on the db
            SQLiteScore sqLiteScore = new SQLiteScore(this, score);
            sqLiteScore.insertScore();

            //Inserting the new score on the Calendar
            LocalCalendar calendar = new LocalCalendar(this);
            calendar.addEvent(score);

            //Deleting puzzle image from data base
            SQLiteGalleryPhoto sqLiteGalleryPhoto = new SQLiteGalleryPhoto(this);
            sqLiteGalleryPhoto.deletePhoto(uImagePath);

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

