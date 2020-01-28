package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.sds.puzzledroid.adapters.ButtonGVAdapter;
import com.sds.puzzledroid.R;

public class SinglePlayerActivity extends AppCompatActivity {

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
    }

    public void onClickGoBack(View view) {
        finish();
    }

}
