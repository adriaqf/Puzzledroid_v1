package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.media.AudioManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sds.puzzledroid.R;
import android.content.SharedPreferences;
import com.sds.puzzledroid.activities.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    SoundPool sp;
    int sound_clic;
    static MediaPlayer mp;
    static MediaPlayer mpdefault;
    int i = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayoutV = findViewById(R.id.LinearLayoutV);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayoutV.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();

        String music_default="android.resource://com.sds.puzzledroid/raw/sparta_music";
        SharedPreferences pref =getSharedPreferences("GlobalSetings",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("music_default",music_default);
        editor.commit();

         sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
         sound_clic = sp.load(this,R.raw.clic,1);

    }

    @Override
    protected void onStart()
    {
        super.onStart();
        audio();
    }
    public void audio(){
        //mira en sharedpreferences si la musica esta activada
        SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        boolean value_music = pref.getBoolean("music_settings",true);
        boolean valueRB = pref.getBoolean("radio_default",true);

        String tipo =pref.getString("UriSong","");
        Uri uri_cho = Uri.parse(tipo);
        Uri uri_def = Uri.parse("android.resource://com.sds.puzzledroid/raw/sparta_music");

        SharedPreferences.Editor editor = pref.edit();
        // editor.putString("Change_UriSong", tipo);
        // editor.putBoolean("radio_default",true);
        //editor.commit();
        String change_UriSong = pref.getString("Change_UriSong",tipo);

        //mp = MediaPlayer.create(getApplicationContext(),uri);
        if(value_music){
            if(valueRB){
                if(mp!=null) {
                    if(mp.isPlaying()) {
                        mp.pause();
                    }
                }
                if(mpdefault==null) {
                    mpdefault = MediaPlayer.create(getApplicationContext(), uri_def);
                }
                if(!mpdefault.isPlaying()){
                    mpdefault.start();
                    mpdefault.setLooping(true);
                }
            }else{
                if(mpdefault!=null) {
                    if(mpdefault.isPlaying()) {
                        mpdefault.pause();
                    }
                }
                if(!change_UriSong.equals(tipo) || i == 0){
                    if(mp!=null){
                        mp.stop();
                        mp.release();
                    }
                    mp = MediaPlayer.create(getApplicationContext(),uri_cho);
                    i=1;
                    editor.putString("Change_UriSong",tipo);
                    editor.commit();
                }
                if(!mp.isPlaying()){
                    mp.start();
                    mp.setLooping(true);
                }
            }
        }else {
            if(mp!=null)
                mp.pause();
            if(mpdefault!=null){
                mp.pause();
            }
        }}

    public void vibrate(){
            SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
            boolean vibrate = pref.getBoolean("sw_vibrate",true);
            Vibrator vibrator=(Vibrator)getApplicationContext() .getSystemService(Context.VIBRATOR_SERVICE);
            if(vibrate){
                vibrator.vibrate(50);
            }
        }

    public void soundPool () {
        SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("effects_sound",true);
        if(value){
            sp.play(sound_clic,1,1,1,0,0);
        }
    }

    public void onClickGoTo(View view) {
        //Mira en sharedpreferences si la opcion esta de efectos de sonidos esta activa
      soundPool();
        vibrate();
        switch(view.getId()){
            case R.id.btn_single_player:
                Intent iSinglePlayer = new Intent(this, SinglePlayerActivity.class);
                startActivity(iSinglePlayer);
                break;
            case R.id.btn_multiplayer:
                Toast.makeText(this, "Estamos trabajando en ello :)", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_help:
                Intent iHelp = new Intent(this, HelpActivity.class);
                startActivity(iHelp);
                break;
            case R.id.btn_config:
                Intent iSettings = new Intent(this, SettingsActivity.class);
                startActivity(iSettings);
                break;
            case R.id.btn_classification:
                Intent iClass = new Intent(this, ClassificationActivity.class);
                iClass.putExtra("difficulty", 3);
                startActivity(iClass);
                break;
            default:
                Toast.makeText(this, "Error: Botón inexistente", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
