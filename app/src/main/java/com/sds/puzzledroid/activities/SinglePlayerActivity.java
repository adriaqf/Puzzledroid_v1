package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.sds.puzzledroid.adapters.ButtonGVAdapter;
import com.sds.puzzledroid.R;

public class SinglePlayerActivity extends AppCompatActivity {
    SoundPool sp;
    int sound_clic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        GridView grid = findViewById(R.id.gidViewLevels);
        grid.setAdapter(new ButtonGVAdapter(this));

        //Toolbar added to activiity's screen
        Toolbar toolbar = findViewById(R.id.toolbar_dynamic);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.bringToFront();

        //Background animation
        LinearLayout linearLayoutV = findViewById(R.id.singlePlayerLinearL);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayoutV.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sound_clic = sp.load(this,R.raw.clic,1);
    }

    public void onClickGoBack(View view) {
        SharedPreferences pref = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("effects_sound",true);
        boolean vibrate = pref.getBoolean("sw_vibrate",true);
        if(value){
            sp.play(sound_clic,1,1,1,0,0);
        }
        Vibrator vibrator=(Vibrator)getApplicationContext() .getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrate){
            vibrator.vibrate(50);
        }
        finish();
    }

}
