package com.sds.puzzledroid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sds.puzzledroid.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity  {

    private Switch music_switch;
    private Switch effect_switch;
    MediaPlayer mediaPlayer = new MediaPlayer();
    Button btn_examinar,btn_play,btn_stop;
    TextView tv_songname;
    private final int PICKER = 1;
    private int State = 1;
    private final int Playing = 1;
    private final int Pausing = 2;
String sname;
    static MediaPlayer mp;
    int position;
    ArrayList<File>mySongs;

    SoundPool sp;
    int sound_Reproduction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btn_examinar =(Button) findViewById(R.id.btn_examinar);
        btn_play =(Button) findViewById(R.id.btn_play);
        btn_stop =(Button) findViewById(R.id.btn_stop);


      //btn_examinar.setOnClickListener(this);
       // btn_play.setOnClickListener(this);
       // btn_stop.setOnClickListener(this);

        /*mediaPlayer = MediaPlayer.create(this,R.raw.sparta_music);
        mediaPlayer.setLooping(true);*/


        effect_switch = (Switch)findViewById(R.id.switch_sp);
        music_switch = (Switch)findViewById(R.id.switch1);


        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sound_Reproduction = sp.load(this,R.raw.gay_sound,1);


        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        effect_switch.setChecked(prefs.getBoolean("effects_sound",true));
        music_switch.setChecked(prefs.getBoolean("music_settings",true));

        prefs.getBoolean("examinar",false);
        SharedPreferences.Editor editor = prefs.edit();

if (prefs.getBoolean("examinar",false)){
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    Intent i = getIntent();
    Bundle bundle = i.getExtras();
    mySongs = (ArrayList) bundle.getParcelableArrayList("songs");

        sname = mySongs.get(position).getName().toString();
        String songName = i.getStringExtra("songname");

        position = bundle.getInt("pos", 0);
        Uri u = Uri.parse(mySongs.get(position).toString());

        mp = MediaPlayer.create(getApplicationContext(), u);

        mp.start();

    }   editor.putBoolean("examinar",false);
        editor.commit();
    }

    public void onClickGo(View view) {

        switch(view.getId()) {
            case R.id.btn_examinar:
                Intent iFileMusic = new Intent(this, FileMusic.class);
                startActivity(iFileMusic);
                finish();
                break;
        }}
    //arrancar un SoundPool
   //sp.play(sound_Reproduction,1,1,1,0,0);
   public void AudioSoundpool(View view) {

       SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
       SharedPreferences.Editor editor = prefs.edit();

       if (effect_switch.isChecked()) {
           editor.putBoolean("effects_sound", true);
       }else {
           editor.putBoolean("effects_sound", false);
       }
       editor.commit();
   }


   public void AudioMediaPlayer(View view){
        SharedPreferences prefs = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if(music_switch.isChecked()) {
            editor.putBoolean("music_settings", true);
        }else{
            editor.putBoolean("music_settings", false);
        }
        editor.commit();
    }
    @Override
    public void onDestroy()
    {
        SharedPreferences prefs = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("examinar", false);
        editor.commit();
        super.onDestroy();

    }
    public void Guardar (View view){ finish();}
/*
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_examinar:
                    PickFile();
                break;
            case R.id.btn_play:
                if (State== 1){
                    mediaPlayer.start();
                    btn_play.setText("PAUSE");
                    State = Pausing;
                }else if(State == 2){
                    mediaPlayer.pause();
                    btn_play.setText("PLAY");
                    State = Playing;
            }
                mediaPlayer.start();
                break;
            case R.id.btn_stop:
                mediaPlayer.stop();

                break;
        }
    }
    private void PickFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try{
            startActivityForResult(Intent.createChooser(intent,"Instale un admin de archivos"),PICKER);
        }catch (android.content.ActivityNotFoundException ex){

        }
    }
@Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
    super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case PICKER:
                if(resultCode == RESULT_OK){
                    Uri FilePath = data.getData();
                    String FilePathAudio = getRealPathFromURI(FilePath);
                    //String FilePath = data.getData().getPath();
                    try{
                        mediaPlayer.setDataSource(FilePathAudio);
                        mediaPlayer.prepare();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
                break;
        }
}
public String getRealPathFromURI(Uri contentUri){
        Cursor cursor = null;
        try{
            String [] proj = {MediaStore.Audio.Media.DATA};
            cursor = getApplicationContext().getContentResolver().query(contentUri,proj,null,null,null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
}*/
    /*  public void AudioMediaPlayer(View view){

            if (!music_switch.isChecked()){
               //mp.stop();
                Toast.makeText(this,"Pausa",Toast.LENGTH_SHORT).show();
            }
            else if (music_switch.isChecked()){
              //  mp.start();
                Toast.makeText(this,"Reproduciendo",Toast.LENGTH_SHORT).show();
            }
    }*/
   /* public void onClickGoTo(View view) {
        switch(view.getId()) {
            case R.id.btn_saveSettings:
                Intent iMain = new Intent(this, MainActivity.class);

                startActivity(iMain);
                break;
        }*/

    /*
    public void loadConfig(){
        SharedPreferences pref = getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        music_switch.setChecked(pref.getBoolean("music_settings",false));
    }
    //en el evento "Cerrar aplicación" guardar los datos en fichero xml
    @Override
    public void onDestroy()
    {
        super.onDestroy();
       // saveConfig();
    }

    //en el evento "Abrir aplicación" leemos los datos de configuración del fichero xml
    @Override
    protected void onResume()
    {
        super.onResume();
        loadConfig();
    }*/
}