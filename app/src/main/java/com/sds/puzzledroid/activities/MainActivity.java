package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.media.AudioManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sds.puzzledroid.R;
import android.content.SharedPreferences;
import com.sds.puzzledroid.activities.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    SoundPool sp;
    int sound_clic;
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

        //crea objeto mediaplayer para el audio y soundpool para los efectos de sonido
        //el vectormp1 esta creado para la opcion que escoja el usuario de su galeria de audio
         vectormp[0] = MediaPlayer.create(this,R.raw.sparta_music);
         vectormp[1] = MediaPlayer.create(this,R.raw.zelda_music);

         sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
         sound_clic = sp.load(this,R.raw.clic,1);

    }
    @Override
    protected void onStart()
    {
        super.onStart();

        //mira en sharedpreferences si la musica esta activada
        SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("music_settings",true);
        //si esta activada empieza la musica en loop, si no comprueba si estaba activada antes de cerrar la app
        //por que
        if(value){
            vectormp[0].start();
            vectormp[0].setLooping(true);
        }else{
            if(vectormp[0].isPlaying()){
                vectormp[0].pause();
            }
        }
    }


    public void onClickGoTo(View view) {
        //Mira en sharedpreferences si la opcion esta de efectos de sonidos esta activa
        SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("effects_sound",true);
        if(value){
            sp.play(sound_clic,1,1,1,0,0);
        }

        switch(view.getId()){
            case R.id.btn_single_player:
                sp.play(sound_clic,1,1,1,0,0);
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
