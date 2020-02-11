package com.sds.puzzledroid.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.activities.MainActivity;

import java.io.IOException;
import java.io.InputStream;

public class MusicService extends Service {
    MediaPlayer mp;
    int currentPos;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        mp = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Gets default music
        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        String uriSongOld = prefs.getString("UriSong", "android.resource://com.sds.puzzledroid/raw/zelda_music");
        // Gets music position (resume)
        currentPos = prefs.getInt("music_currentPosition",0);
        // Playing song
        String uriSongNew = prefs.getString("ChangeUriSong","");
        Uri uri_cho = Uri.parse(uriSongOld);
        // Music switch button (status)
        boolean music_vaule = prefs.getBoolean("music_settings", true);

        if(music_vaule) { // Music button is "ON"
            mp = MediaPlayer.create(getApplicationContext(), uri_cho);
            if(!mp.isPlaying()) {
                if(uriSongOld.equals(uriSongNew)) { // New song and old song are equal
                    mp.seekTo(currentPos);
                }
                mp.start();
                mp.setLooping(true);
            }
        }

        return super.onStartCommand(intent,flags,startId);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Playing song is saved
        String uriSongOld = prefs.getString("UriSong", "");
        editor.putString("ChangeUriSong", uriSongOld);
        // Actual music position is saved
        currentPos = mp.getCurrentPosition();
        editor.putInt("music_currentPosition", currentPos);
        editor.apply();

        mp.stop();
        mp.release();
    }

}
