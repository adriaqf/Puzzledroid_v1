package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.utils.FBFirestore;

public class ClassificationActivity extends AppCompatActivity {

    SoundPool sp;
    int sound_clic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);

        FrameLayout frameLayout = findViewById(R.id.fl_header_classification);
        ViewCompat.setTranslationZ(frameLayout, 1);

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sound_clic = sp.load(this,R.raw.clic,1);


        //Modifying onClick button's event (toolbar_bottom)
        Toolbar toolbar = findViewById(R.id.toolbar_dynamic);
        ImageButton backBtn = toolbar.findViewById(R.id.btn_back_home);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
                boolean value = pref.getBoolean("effects_sound",true);if(value){
                    sp.play(sound_clic,1,1,1,0,0);
                }
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Cargando");
        progress.setMessage("Este proceso puede tardar varios segundos ...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        // Gets top ten scores
        ListView listView = findViewById(R.id.lv_classification);
        listView.setOnItemClickListener(null);
        FBFirestore fb = new FBFirestore();
        fb.getTopTenScores(this, listView, progress);

    }
}
