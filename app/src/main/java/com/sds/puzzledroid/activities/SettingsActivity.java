package com.sds.puzzledroid.activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.sds.puzzledroid.R;

public class SettingsActivity extends AppCompatActivity {

    Switch music;
    SoundPool sp;
    int sound_Reproduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        music = (Switch)findViewById(R.id.switch1);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sound_Reproduction = sp.load(this,R.raw.gay_sound,1);

    }
    public void AudioSoundPool(View view){
        sp.play(sound_Reproduction,1,1,1,0,0);
    }
    public void AudioMediaPlayer(View view){
        MediaPlayer mp = MediaPlayer.create(this,R.raw.zelda_music);
        if(view.getId()== R.id.switch1){
            if (music.isChecked()){
                mp.start();
            }
            else if(!music.isChecked()){
                mp.stop();
                mp.release();
            }
        }


    }
}