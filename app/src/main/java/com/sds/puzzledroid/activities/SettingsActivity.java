package com.sds.puzzledroid.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sds.puzzledroid.R;

public class SettingsActivity extends AppCompatActivity {

    private Switch music_switch;
    SoundPool sp;
    MediaPlayer mp;
    int sound_Reproduction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        music_switch = (Switch)findViewById(R.id.switch1);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sound_Reproduction = sp.load(this,R.raw.gay_sound,1);


        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
       music_switch.setChecked(prefs.getBoolean("music_settings",false));

    }
   public void AudioSoundPool(View view){
        sp.play(sound_Reproduction,1,1,1,0,0);
    }

    public void AudioMediaPlayer(View view){
            if (!music_switch.isChecked()){
                mp.stop();
                Toast.makeText(this,"Pausa",Toast.LENGTH_SHORT).show();
            }
            else if (music_switch.isChecked()){
                mp.start();
                Toast.makeText(this,"Reproduciendo",Toast.LENGTH_SHORT).show();
            }
    }

   public void saveConfig(){
        SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
       Toast.makeText(this,"config",Toast.LENGTH_SHORT);
        if(music_switch.isChecked()) {
            editor.putBoolean("music_settings", true);
            Toast.makeText(this,"True",Toast.LENGTH_SHORT);
        }
        else if(!music_switch.isChecked()){
            editor.putBoolean("music_settings", false);
            Toast.makeText(this,"false",Toast.LENGTH_SHORT);
        }
        editor.commit();
       // editor.putInt("music_settings",)
    }
    public void loadConfig(){
        SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        music_switch.setChecked(pref.getBoolean("music_settings",false));
    }
    //en el evento "Cerrar aplicación" guardar los datos en fichero xml
    @Override
    public void onDestroy()
    {   Toast.makeText(this,"Saved",Toast.LENGTH_SHORT);
        super.onDestroy();
        saveConfig();

    }

    //en el evento "Abrir aplicación" leemos los datos de configuración del fichero xml
    @Override
    protected void onStart()
    {
        super.onStart();
        loadConfig();
    }
}