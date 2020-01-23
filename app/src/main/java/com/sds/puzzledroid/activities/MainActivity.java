package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sds.puzzledroid.R;

public class MainActivity extends AppCompatActivity {

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
                Toast.makeText(this, "Configuración no disponible.", Toast.LENGTH_SHORT).show();
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
