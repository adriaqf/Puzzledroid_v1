package com.sds.puzzledroid.activities;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sds.puzzledroid.logic.ImagePuzzle;
import com.sds.puzzledroid.logic.Jigsaw;
import com.sds.puzzledroid.logic.LocalCalendar;
import com.sds.puzzledroid.logic.PuzzlePiece;
import com.sds.puzzledroid.R;
import com.sds.puzzledroid.logic.TouchListener;
import com.sds.puzzledroid.logic.Score;
import com.sds.puzzledroid.sqlite.SQLiteGalleryPhoto;
import com.sds.puzzledroid.sqlite.SQLiteScore;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;

import static android.app.Notification.DEFAULT_SOUND;
import static com.sds.puzzledroid.activities.App.CHANNEL_1_ID;

public class JigsawActivity extends AppCompatActivity {
    private Jigsaw jigsaw;
    private Chronometer chronometer;
    private int totalScore;
    private int localDifficulty;
    private Uri uImagePath;

    private NotificationManagerCompat managerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw);

        managerCompat = NotificationManagerCompat.from(this);

        final RelativeLayout layout = findViewById(R.id.rLayoutPuzzleLevel);
        final ImageView imageView = findViewById(R.id.ivPuzzle);
        chronometer = findViewById(R.id.chrono);

        ImagePuzzle imagePuzzle = new ImagePuzzle(this, imageView);
        uImagePath = imagePuzzle.randomizeJigsawImage();

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
        finish();
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
                .setContentTitle("¡COMPLETADO!")
                .setContentText("Haz click aquí para visualizar tu puntuación.")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setTimeoutAfter(5000)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .build();

        managerCompat.notify(1, notification);
    }

    public void checkGameOver() {
        if (jigsaw.isJigsawCompleted()) {

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
        }
    }

    public int getChronometerSeconds() {
        int pos0, pos1, pos3, pos4, total;
        String sChronometer;
        CharSequence timeSecs;

        timeSecs = chronometer.getText();
        sChronometer = timeSecs.toString();
        pos0 = Integer.parseInt(String.valueOf(sChronometer.charAt(0)))*10*60;
        pos1 = Integer.parseInt(String.valueOf(sChronometer.charAt(1)))*60;
        pos3 = Integer.parseInt(String.valueOf(sChronometer.charAt(3)))*10;
        pos4 = Integer.parseInt(String.valueOf(sChronometer.charAt(4)));

        total = pos0+pos1+pos3+pos4;

        return total;
    }

}

