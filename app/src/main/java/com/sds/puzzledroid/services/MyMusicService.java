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

public class MyMusicService extends Service {
    MediaPlayer mp;
    int currentPos;

    @Override
    public void onCreate(){
        super.onCreate();

       mp = new MediaPlayer();
       mp.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        String tipo = prefs.getString("UriSong", "");
        currentPos = prefs.getInt("music_currentPosition",0);
        String tipo2 = prefs.getString("ChangeUriSong","");
        Uri uri_cho = Uri.parse(tipo);
        boolean music_vaule = prefs.getBoolean("music_settings", true);
        boolean onPause = prefs.getBoolean("pause",true);

    if(music_vaule){
        mp = MediaPlayer.create(getApplicationContext(), uri_cho);

            if(!mp.isPlaying()){
                if(tipo.equals(tipo2)){
                    mp.seekTo(currentPos);
                }

                mp.start();
                mp.setLooping(true);
            }
    }




        return super.onStartCommand(intent,flags,startId);
    }
    public void onPause(){

    }
   @Override
    public void onDestroy(){
        super.onDestroy();
       SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = prefs.edit();
       String tipo = prefs.getString("UriSong", "");
       editor.putString("ChangeUriSong",tipo);

       currentPos = mp.getCurrentPosition();
       editor.putInt("music_currentPosition", currentPos);
       editor.commit();

       boolean onPause = prefs.getBoolean("pause",true);
       if(!onPause) {
           mp.stop();
           mp.release();
       }else
       {
           mp.pause();
       }

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
