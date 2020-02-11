package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.media.AudioManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.sds.puzzledroid.R;

import com.sds.puzzledroid.services.MusicService;
import com.sds.puzzledroid.adapters.ItemMainLVAdapter;
import com.sds.puzzledroid.pojos.InternalGallery;
import com.sds.puzzledroid.pojos.LCalendarEvent;
import com.sds.puzzledroid.pojos.LocalCalendar;
import com.sds.puzzledroid.pojos.Permissions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SoundPool sp;
    private int clickSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getCalendarEventsScores();

        // Background Animation
        LinearLayout linearLayoutV = findViewById(R.id.LinearLayoutV);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayoutV.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        // Brings Toolbar to front
        Toolbar toolbar = findViewById(R.id.ToolBar);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();

        SharedPreferences pref = getSharedPreferences("GlobalSettings", MODE_PRIVATE);
        // By first time sounds Spartan music
        SharedPreferences.Editor editor = pref.edit();
        pref.getBoolean("music_settings", true);
        editor.apply();

        // Configurates button's sound
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        clickSound = sp.load(this, R.raw.clic, 1);

        //When you open the app, the music will start at the begining
        editor.putInt("music_currentPosition", 0);
        editor.commit();

        // Starts the MusicService service
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        startService(intent);

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
                Intent iSettings = new Intent(this, SettingsActivity.class);
                startActivity(iSettings);
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
        //Sound and vibrate effects
        soundPool();
        vibrate();
    }

    public void vibrate(){
        SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        boolean vibrateActive = pref.getBoolean("sw_vibrate",true);
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrateActive) { //If vibration is activated in Settings
            vibrator.vibrate(50);
        }
    }

    public void soundPool () {
        SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        boolean soundActive = pref.getBoolean("effects_sound",true);
        if(soundActive) { //If sound is activated in Settings
            sp.play(clickSound,1,1,1,0,0);
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
