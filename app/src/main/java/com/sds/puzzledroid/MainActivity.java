package com.sds.puzzledroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayoutV = (LinearLayout) findViewById(R.id.LinearLayoutV);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayoutV.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        Toolbar toolbar = (Toolbar) findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.bringToFront();
    }

    //Método boton Individual
    public void individual(View view){
        Intent individual = new Intent(this,IndividualActivity.class);
        startActivity(individual);
    }
    //Método boton Multijugador
    public void multijugador(View view){
        Intent multijugador = new Intent(this,MultijugadorActivity.class);
        startActivity(multijugador);
    }

    //Shows Action Bar Menu
    /*public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }*/

    //Adds Action Bar Buttons' actions
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.config_item:
                Toast.makeText(this, "En construcción ...", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.help_item:
                onClickGoTo(this.findViewById(R.id.help_item));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Go to another Activity
    public void onClickGoTo(View view) {
        Intent i = new Intent(this, HelpActivity.class);
        startActivity(i);
    }
}
