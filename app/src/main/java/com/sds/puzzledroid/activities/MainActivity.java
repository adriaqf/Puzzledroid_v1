package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sds.puzzledroid.R;
import android.content.SharedPreferences;
import com.sds.puzzledroid.activities.SettingsActivity;

public class MainActivity extends AppCompatActivity {


    MediaPlayer vectormp [] = new MediaPlayer [2];


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

         vectormp[0] = MediaPlayer.create(this,R.raw.sparta_music);
         vectormp[1] = MediaPlayer.create(this,R.raw.zelda_music);






        //Chequeo de las opciones de settings
         int music_bakcground;
       SharedPreferences prefs = getSharedPreferences("files_settings", Context.MODE_PRIVATE);
        music_bakcground = prefs.getInt("music",1);

    }
    @Override
    protected void onStart()
    {
        super.onStart();
        SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("music_settings",true);


        if(value){
            vectormp[0].start();
            vectormp[0].setLooping(true);
        }
        else{
            if(vectormp[0].isPlaying()){
                vectormp[0].pause();
            }


        }
    }



    public void onClickGoTo(View view) {
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
                //iSettings.putExtra("vector", vectormp);
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
