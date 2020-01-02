package com.sds.puzzledroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

public class SinglePlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);

        AssetManager am = getAssets();
        try {
            final String[] files  = am.list("img");

            GridView grid = findViewById(R.id.gidViewLevels);
            grid.setAdapter(new ButtonAdapter(this));
            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), PuzzleLevelActivity.class);
                    intent.putExtra("assetName", files[i % files.length]);
                    startActivity(intent);
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT);
        }

        Toolbar toolbar = findViewById(R.id.toolbar_dynamic);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.bringToFront();

        //GidView buttons added
       GridView gridView = findViewById( R.id.gidViewLevels);
        gridView.setAdapter(new ButtonAdapter(this));

        LinearLayout linearLayoutV = findViewById(R.id.singlePlayerLinearL);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayoutV.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();


    }


    public void onClickGoTo(View view) {
        switch(view.getId()){
            case R.id.btn_back_home:
                finish();
                break;
            case R.id.btn_classification:
                Toast.makeText(this, "Estamos trabajando en ello :)", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Error: Botón inexistente", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
