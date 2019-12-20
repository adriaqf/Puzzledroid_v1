package com.sds.puzzledroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    //Shows Action Bar Menu
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

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
