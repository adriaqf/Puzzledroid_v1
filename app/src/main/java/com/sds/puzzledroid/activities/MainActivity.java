package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.adapters.ItemClassificationLVAdapter;
import com.sds.puzzledroid.adapters.ItemMainLVAdapter;
import com.sds.puzzledroid.logic.InternalGallery;
import com.sds.puzzledroid.logic.LCalendarEvent;
import com.sds.puzzledroid.logic.LocalCalendar;
import com.sds.puzzledroid.logic.Permissions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCalendarEventsScores();

        LinearLayout linearLayoutV = findViewById(R.id.LinearLayoutV);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayoutV.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();

        // Verifies if the user has already granted the access to its external storage
        // for gallery access
        Permissions permissions = new Permissions(this);
        permissions.verifyPermissions();

        //Load all gallery photos from user
        //Permission needed READ_EXTERNAL_STORAGE
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!preferences.getBoolean("firstTime", false)) {
            //Takes all gallery photos from the phone
            InternalGallery internalGallery = new InternalGallery(this);
            internalGallery.saveFullGallery();
            //Creates a new calendar to save later scores
            LocalCalendar localCalendar = new LocalCalendar(this);
            localCalendar.addNewCalendar();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();
            System.out.println("Galería cargada.");
        }
        else {
            System.out.println("Ya no se carga la galería.");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        ListView listView = findViewById(R.id.lv_main);
        listView.setOnItemClickListener(null);
        ItemMainLVAdapter itemMainLVAdapter = new ItemMainLVAdapter(this, getCalendarEventsScores());
        listView.setAdapter(itemMainLVAdapter);
    }

    public void onClickGoTo(View view) {
        switch(view.getId()){
            case R.id.btn_single_player:
                Intent iSinglePlayer = new Intent(this, SinglePlayerActivity.class);
                startActivity(iSinglePlayer);
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

    private ArrayList<LCalendarEvent> getCalendarEventsScores() {
        ArrayList<LCalendarEvent> pEvents;
        ArrayList<LCalendarEvent> events = new ArrayList<>();
        LocalCalendar calendar = new LocalCalendar(this);
        pEvents = calendar.getAllEvents();

        try {
            for(int i = 1; i <= 3; i++) {
                events.add(pEvents.get(pEvents.size() - i));
            }
            return events;
        } catch (ArrayIndexOutOfBoundsException e) {
            return events;
        }

    }
}
