package com.sds.puzzledroid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.logic.MusicExplorer;
import com.sds.puzzledroid.services.MusicService;

import java.io.File;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity  {

    Switch effect_switch, music_switch, switch_vibrate;
    TextView tv_songname;
    RadioButton rb_default, rb_select;

    //Used to select own music
    ArrayList<File> mySongs;
    int position;
    //Sound effects
    SoundPool sp;
    int sound_Reproduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tv_songname = findViewById(R.id.tv_songname);
        rb_default = findViewById(R.id.rb_default);
        rb_select = findViewById(R.id.rb_select);
        switch_vibrate= findViewById(R.id.switch_vibrate);
        effect_switch = findViewById(R.id.switch_sp);
        music_switch = findViewById(R.id.switch1);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sound_Reproduction = sp.load(this,R.raw.gay_sound,1);

        //All switch buttons are true by default
        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        effect_switch.setChecked(prefs.getBoolean("effects_sound",true));
        music_switch.setChecked(prefs.getBoolean("music_settings",true));
        rb_default.setChecked(prefs.getBoolean("radio_default",true));
        rb_select.setChecked(!prefs.getBoolean("radio_default",true));
        switch_vibrate.setChecked(prefs.getBoolean("sw_vibrate",true));

        toExploreMusicStorage();
    }

    public void onClickGo(View view) {
        switch(view.getId()) {
            case (R.id.rb_select):
                if(rb_select.isChecked()){
                    rb_default.setChecked(false);
                    SharedPreferences prefs = getSharedPreferences("GlobalSettings", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("radio_default", false);
                    editor.apply();
                    Intent iFileMusic = new Intent(this, MusicExplorer.class);
                    startActivity(iFileMusic);
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
                    editor.apply();

                    Intent intent = new Intent(SettingsActivity.this, MusicService.class);
                    stopService(intent);
                    startService(intent);

                    tv_songname.setText("Música: Por defecto");
                    break;
                }

            case (R.id.btn_saveSettings):
                finish();
                break;
        }
    }

    // Explores external storage looking for music files
    public void toExploreMusicStorage() {
        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        prefs.getBoolean("examinar",false);
        SharedPreferences.Editor editor = prefs.edit();

        if (prefs.getBoolean("examinar",false)){
            Intent i = getIntent();
            //Mapping from String keys to various Parcelable values
            Bundle bundle = i.getExtras();
            //Gets all strings from bundle with "songs" key
            mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
            //Gets song's name from it's position
            String songName = i.getStringExtra("songname");
            position = bundle.getInt("pos", 0);

            tv_songname.setText(songName);
            tv_songname.setSelected(true);

            editor.putString("UriSong", mySongs.get(position).toString());
            editor.apply();

            if(prefs.getBoolean("music_settings",true)){
                Intent intent = new Intent(SettingsActivity.this, MusicService.class);
                stopService(intent);
                startService(intent);
            }
        }

        //Don't allow access if switch not pressed
        editor.putBoolean("examinar", false);
        editor.commit();
    }

    //Manages audio soundpool switch button
    public void AudioSoundpool(View view) {

       SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = prefs.edit();

       if (effect_switch.isChecked()) {
           editor.putBoolean("effects_sound", true);
       } else {
           editor.putBoolean("effects_sound", false);
       }
       editor.apply();
    }


    //Manages music switch button
    public void AudioMediaPlayer(View view){
        SharedPreferences prefs = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Intent intent = new Intent(SettingsActivity.this, MusicService.class);

        if(music_switch.isChecked()) {
            editor.putBoolean("music_settings", true);
            startService(intent);
        } else {
            editor.putBoolean("music_settings", false);
            stopService(intent);
        }
        editor.apply();
    }

    //Manages vibration switch button
    public void switchVibrate(View view){
        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (switch_vibrate.isChecked()) {
            editor.putBoolean("sw_vibrate", true);
        } else {
            editor.putBoolean("sw_vibrate", false);
        }
        editor.apply();
    }

    @Override
    public void onDestroy() {
        SharedPreferences prefs = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("examinar", false);
        editor.apply();
        super.onDestroy();

    }

}