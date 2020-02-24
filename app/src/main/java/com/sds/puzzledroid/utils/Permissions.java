package com.sds.puzzledroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CALENDAR;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_CALENDAR;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Permissions {

    private Context context;

    private static final int MY_PERMISSIONS = 100;

    private final String[] permissions = new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, READ_CALENDAR, WRITE_CALENDAR};

    public Permissions(Context context) {
        this.context = context;
    }

    public void verifyPermissions() {
        if (ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, READ_CALENDAR) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            //Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, READ_CALENDAR) ||
                    ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, WRITE_CALENDAR)) {
                Toast.makeText(context, "Necesitas aceptar varios permisos para utilizar Puzzledroid.", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions((Activity) context, permissions, MY_PERMISSIONS);
            }
        }
    }

}