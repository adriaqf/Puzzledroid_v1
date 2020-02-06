package com.sds.puzzledroid.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.sds.puzzledroid.R;

import java.io.File;
import java.util.ArrayList;

public class FileMusic extends AppCompatActivity {

    ListView myListVewForSongs;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fileselect);

        myListVewForSongs = (ListView) findViewById(R.id.mySongListView);
        SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("examinar",false);
        editor.commit();
        runtimePermission();
    }

    public void runtimePermission(){
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                display();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }
    public ArrayList<File> findSong(File file) {
        ArrayList<File> arrayList = new ArrayList<>();

        File[] files = file.listFiles();
        try {

            for (File singleFile : files) {

                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    arrayList.addAll(findSong(singleFile));
                } else {
                    if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")) {
                        arrayList.add(singleFile);
                    }
                }

            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"No tienes archivos Mp3",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
        }
        return arrayList;

       }
    void display(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());


        items = new String[mySongs.size()];

        for(int i=0;i<mySongs.size();i++){
            items[i] = mySongs.get(i).getName().replace(".mp3","").replace(".wav","");

        }

        ArrayAdapter<String>myAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items);
        myListVewForSongs.setAdapter(myAdapter);

        myListVewForSongs.setOnItemClickListener (new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                String songName = myListVewForSongs.getItemAtPosition(i).toString();
                if(mySongs.isEmpty()){

                }else{
                startActivity(new Intent(getApplicationContext(),SettingsActivity.class)
                        .putExtra("songs",mySongs)
                        .putExtra("songname",songName)
                        .putExtra("pos",i));


                //evita error
                SharedPreferences prefs = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("examinar",true);
                editor.commit();
                finish();

            }}

        });
    }
}