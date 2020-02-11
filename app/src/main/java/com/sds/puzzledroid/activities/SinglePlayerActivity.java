package com.sds.puzzledroid.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.sds.puzzledroid.adapters.ButtonGVAdapter;
import com.sds.puzzledroid.R;
import com.sds.puzzledroid.sqlite.SQLiteGalleryPhoto;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SinglePlayerActivity extends AppCompatActivity {
    SoundPool sp;
    int sound_clic;

    private static final int REQUEST_TAKE_PHOTO = 1;
    private Uri currentPhotoUri;

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

        sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        sound_clic = sp.load(this, R.raw.clic, 1);

    }

    public void onClick(View view) {
        SharedPreferences pref = getSharedPreferences("GlobalSettings", Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("effects_sound", true);
        boolean vibrate = pref.getBoolean("sw_vibrate", true);

        switch (view.getId()) {
            case R.id.btn_back_home:
                finish();
                break;

            case R.id.btn_add_photo:
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(this,
                                "com.puzzledroid.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }
        }

        if (value) {
            sp.play(sound_clic, 1, 1, 1, 0, 0);
        }
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrate) {
            vibrator.vibrate(50);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoUri = Uri.fromFile(image);
        addPhotoToInternalGallery();
        return image;
    }

    private void addPhotoToInternalGallery() {
        SQLiteGalleryPhoto sqLiteGalleryPhoto = new SQLiteGalleryPhoto(this, currentPhotoUri);
        sqLiteGalleryPhoto.insertPhoto();
    }
}
