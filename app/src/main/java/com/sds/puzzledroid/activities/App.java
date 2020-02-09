package com.sds.puzzledroid.activities;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_1_ID = "gameOver";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationsChannels();
    }

    private void createNotificationsChannels() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel cGameOver = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Game Over",
                    NotificationManager.IMPORTANCE_HIGH
            );
            cGameOver.setDescription("Notify user that the jigsaw has been completed.");
            cGameOver.enableLights(true);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(cGameOver);
        }
    }

}
