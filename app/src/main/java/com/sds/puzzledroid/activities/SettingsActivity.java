package com.sds.puzzledroid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sds.puzzledroid.R;

public class SettingsActivity extends AppCompatActivity {

    private Switch music_switch;
    private Switch effect_switch;
    SoundPool sp;
    int sound_Reproduction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        effect_switch = (Switch)findViewById(R.id.switch_sp);
        music_switch = (Switch)findViewById(R.id.switch1);


        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sound_Reproduction = sp.load(this,R.raw.gay_sound,1);


        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        effect_switch.setChecked(prefs.getBoolean("effects_sound",true));
        music_switch.setChecked(prefs.getBoolean("music_settings",true));



    }
    //arrancar un SoundPool
   //sp.play(sound_Reproduction,1,1,1,0,0);
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

        if(music_switch.isChecked()) {
            editor.putBoolean("music_settings", true);
        }else{
            editor.putBoolean("music_settings", false);
        }
        editor.commit();
    }
    public void Guardar (View view){ finish();}


    /*  public void AudioMediaPlayer(View view){

            if (!music_switch.isChecked()){
               //mp.stop();
                Toast.makeText(this,"Pausa",Toast.LENGTH_SHORT).show();
            }
            else if (music_switch.isChecked()){
              //  mp.start();
                Toast.makeText(this,"Reproduciendo",Toast.LENGTH_SHORT).show();
            }
    }*/
   /* public void onClickGoTo(View view) {
        switch(view.getId()) {
            case R.id.btn_saveSettings:
                Intent iMain = new Intent(this, MainActivity.class);

                startActivity(iMain);
                break;
        }*/

    /*
    public void loadConfig(){
        SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        music_switch.setChecked(pref.getBoolean("music_settings",false));
    }
    //en el evento "Cerrar aplicación" guardar los datos en fichero xml
    @Override
    public void onDestroy()
    {
        super.onDestroy();
       // saveConfig();
    }

    //en el evento "Abrir aplicación" leemos los datos de configuración del fichero xml
    @Override
    protected void onResume()
    {
        super.onResume();
        loadConfig();
    }*/
}