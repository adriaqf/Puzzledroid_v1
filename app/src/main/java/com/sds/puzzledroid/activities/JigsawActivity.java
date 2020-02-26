package com.sds.puzzledroid.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;

import android.app.Notification;
import android.app.PendingIntent;

import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.StorageReference;
import com.sds.puzzledroid.GlideApp;
import com.sds.puzzledroid.listeners.TouchListener;
import com.sds.puzzledroid.pojos.Jigsaw;
import com.sds.puzzledroid.utils.FBFirestore;
import com.sds.puzzledroid.utils.LocalCalendar;
import com.sds.puzzledroid.pojos.PuzzleImage;
import com.sds.puzzledroid.R;

import com.sds.puzzledroid.pojos.PuzzlePiece;
import com.sds.puzzledroid.pojos.Score;

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

    private RelativeLayout layout;
    private ImageView ivPuzzle;
    private ImageView ivPuzzleBG;
    private ImageView ivWin;
    private Animation animZoomInBlind, zoomOutIn;
    private Animation alpha;

    private SoundPool sp;
    private int succesfullSound;
    private int clickSound;

    private NotificationManagerCompat managerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw);

        managerCompat = NotificationManagerCompat.from(this);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        succesfullSound = sp.load(this, R.raw.succefull, 1);
        clickSound = sp.load(this, R.raw.clic, 1);

        layout = findViewById(R.id.rLayoutPuzzleLevel);
        ivPuzzle = findViewById(R.id.ivPuzzle);
        chronometer = findViewById(R.id.chrono);

        loadImageView();

        ivPuzzleBG = findViewById(R.id.ivPuzzle); // Alpha background (puzzle)
        ivWin = findViewById(R.id.ivwin); // Victory stars (for animation)

        animZoomInBlind = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_in_blind); // Parpadeo de stars
        alpha = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.keep_in_alpha_0); // Puts stars in alpha 0
        zoomOutIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_in); // Zoom out + in

        ivWin.startAnimation(alpha); // Hides stars picture

        Intent intent = getIntent();
        this.localDifficulty = intent.getIntExtra("levelDifficulty", 1);

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
            sp.play(succesfullSound, 1, 1, 1, 0, 0);
        }
    }

    public void soundPoolBtn() {
        SharedPreferences pref = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("effects_sound", true);

        if (value) {
            sp.play(clickSound, 1, 1, 1, 0, 0);
        }
    }

    private void loadImageView() {
        // Gets randomized image
        PuzzleImage puzzleImage = new PuzzleImage(this, ivPuzzle, jigsaw, localDifficulty, layout, this);
        StorageReference img = puzzleImage.randomizeJigsawImage();
        // Loads randomized image to imageview (!!!Async task!!!)
        GlideApp.with(this)
                .load(img)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        ivPuzzle.post(new Runnable() {
                            @SuppressLint("ClickableViewAccessibility")
                            @Override
                            public void run() {
                                loadJigsawPieces();
                            }

                        });
                        return false;
                    }})
                .into(ivPuzzle);
    }

    public void loadJigsawPieces() {
        jigsaw = new Jigsaw(getApplicationContext(), ivPuzzle, localDifficulty);
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
            ivPuzzleBG.setAlpha(1f);
            ivPuzzleBG.bringToFront();
            ivPuzzleBG.startAnimation(zoomOutIn);
            ivWin.startAnimation(animZoomInBlind);
            //Completed puzzle sound
            soundPoolJigsawComplete();

            chronometer.stop();
            totalScore = getChronometerSeconds();

            sendNotificationCGameOver();

            Score score = new Score(totalScore, localDifficulty);

            //Inserting the new score on the Calendar
            LocalCalendar calendar = new LocalCalendar(this);
            calendar.addEvent(score);

            //Inserting the new score on the Firestore
            FBFirestore fb = new FBFirestore();
            fb.addScoreDoc(score);

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
                        ivWin.startAnimation(alpha);
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

