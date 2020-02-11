package com.sds.puzzledroid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.logic.Settings;
import com.sds.puzzledroid.services.MyMusicService;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity  {

    Switch effect_switch,music_switch,switch_vibrate;
    TextView tv_songname;
    RadioButton rb_default,rb_select;
    boolean algo;

    //Recojen valores de STORAGE SD
    String sname;
    static MediaPlayer mp;
    int position;
    ArrayList<File>mySongs;

    //SONIDOS CORTOS
    SoundPool sp;
    int sound_Reproduction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tv_songname = (TextView)findViewById(R.id.tv_songname);
        rb_default = (RadioButton)findViewById(R.id.rb_default);
        rb_select = (RadioButton)findViewById(R.id.rb_select);
        switch_vibrate= (Switch)findViewById(R.id.switch_vibrate);
        effect_switch = (Switch)findViewById(R.id.switch_sp);
        music_switch = (Switch)findViewById(R.id.switch1);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sound_Reproduction = sp.load(this,R.raw.gay_sound,1);

        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        effect_switch.setChecked(prefs.getBoolean("effects_sound",true));
        music_switch.setChecked(prefs.getBoolean("music_settings",true));
        rb_default.setChecked(prefs.getBoolean("radio_default",true));
        rb_select.setChecked(!prefs.getBoolean("radio_default",true));
        switch_vibrate.setChecked(prefs.getBoolean("sw_vibrate",true));
        Intent intent = new Intent(SettingsActivity.this, MyMusicService.class);
       // startService(intent);
        explorar();


    }
   public void explorar(){
        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        prefs.getBoolean("examinar",false);
        SharedPreferences.Editor editor = prefs.edit();

        //SETTINGS RESET SI PETA LA APP
        // editor.putBoolean("examinar",false);
        // editor.commit();

        if (prefs.getBoolean("examinar",false)){
            if (mp != null) {
                mp.stop();
                mp.release();
            }
            Intent i = getIntent();
            Bundle bundle = i.getExtras();
            mySongs = (ArrayList) bundle.getParcelableArrayList("songs");

            sname = mySongs.get(position).getName().toString();
            String songName = i.getStringExtra("songname");

            position = bundle.getInt("pos", 0);

            Uri u = Uri.parse(mySongs.get(position).toString());
            String a;
            a = u.toString();

            tv_songname.setText(songName);
            tv_songname.setSelected(true);
            // mp = MediaPlayer.create(getApplicationContext(), u);
            // mp.start();

            editor.putString("UriSong",a);
            editor.commit();
            boolean value = prefs.getBoolean("music_settings",true);
            if(value){
                Intent intent = new Intent(SettingsActivity.this, MyMusicService.class);
                stopService(intent);
                startService(intent);
            }
        }

        editor.putBoolean("examinar",false);
        editor.commit();
    }
   public void onClickGo(View view) {

        switch(view.getId()) {
            case (R.id.rb_select):
                if(rb_select.isChecked()){
                    rb_default.setChecked(false);
                    SharedPreferences prefs =getSharedPreferences("GlobalSettings",MODE_PRIVATE);
                    SharedPreferences.Editor editor=prefs.edit();
                    editor.putBoolean("radio_default",false);
                    editor.commit();
                    Intent iFileMusica = new Intent(this, FileMusic.class);
                    startActivity(iFileMusica);
                    finish();
                    break;
                }
            case (R.id.rb_default):
                if(rb_default.isChecked()){
                    rb_select.setChecked(false);

                    SharedPreferences prefs =getSharedPreferences("GlobalSettings",MODE_PRIVATE);
                    SharedPreferences.Editor editor=prefs.edit();
                    editor.putBoolean("radio_default",true);
                    editor.putString("UriSong","android.resource://com.sds.puzzledroid/raw/sparta_music");
                    editor.commit();
                    boolean value = prefs.getBoolean("music_settings",true);

                    if(value){
                        Intent intent = new Intent(SettingsActivity.this, MyMusicService.class);
                        stopService(intent);
                        startService(intent);
                    }
                    tv_songname.setText("Musica: Por defecto");
                    break;
                }
        }
  }
   public void AudioSoundpool(View view) {

       SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = prefs.edit();

       if (effect_switch.isChecked()) {
           editor.putBoolean("effects_sound", true);
       }else {
           editor.putBoolean("effects_sound", false);
       }
       editor.commit();
   }


   public void AudioMediaPlayer(View view){
        SharedPreferences prefs = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Intent intent = new Intent(SettingsActivity.this, MyMusicService.class);

        if(music_switch.isChecked()) {
            editor.putBoolean("music_settings", true);
            startService(intent);
        }else{
            editor.putBoolean("music_settings", false);
            stopService(intent);
        }
       editor.commit();
    }
 /*   @Override
    protected void onStop()
    {
        SharedPreferences pref = getSharedPreferences("GlobalSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("pause",true);
        editor.commit();
        Intent intent = new Intent(SettingsActivity.this, MyMusicService.class);
        stopService(intent);
        algo = true;
        super.onStop();
        // audio();
    }

    @Override
    protected void onResume()

    {
        SharedPreferences pref = getSharedPreferences("GlobalSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("pause",false);
        editor.commit();
        Intent intent = new Intent(SettingsActivity.this, MyMusicService.class);
        if(algo){
            startService(intent);
            algo = false;
        }
        super.onResume();
        // audio();
    }*/

    public void switchVibrate(View view){
    SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();

       if (switch_vibrate.isChecked()) {
        editor.putBoolean("sw_vibrate", true);
    }else {
        editor.putBoolean("sw_vibrate", false);
    }
       editor.commit();
    }
    @Override
    public void onDestroy()
    {
        SharedPreferences prefs = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("examinar", false);
        editor.commit();
        super.onDestroy();

    }
    public void Guardar (View view){ finish();}
}