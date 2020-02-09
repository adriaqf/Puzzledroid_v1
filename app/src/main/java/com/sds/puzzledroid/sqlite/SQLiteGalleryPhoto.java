package com.sds.puzzledroid.sqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Locale;

public class SQLiteGalleryPhoto {

    private Context context;
    private ArrayList<Uri> allImagesPath;
    private Uri imagePath;
    private AdminOpenHelper adminOpenHelper;

    public SQLiteGalleryPhoto(Context context, ArrayList<Uri> allImagesPath) {
        this.context = context;
        this.allImagesPath = allImagesPath;
    }

    public SQLiteGalleryPhoto(Context context, Uri imagePath) {
        this.context = context;
        this.imagePath = imagePath;
    }

    public SQLiteGalleryPhoto(Context context) {
        this.context = context;
    }

    public void insertHoleGallery() {
        adminOpenHelper = new AdminOpenHelper(context);
        SQLiteDatabase database = adminOpenHelper.getWritableDatabase();

        for(int i = 0; i < allImagesPath.size(); i++) {
            String imagePath = allImagesPath.get(i).toString();
            System.out.println("Antes de entrar en la db: " + imagePath);
            database.execSQL(String.format(Locale.getDefault(), "INSERT INTO PhotoGallery(path) VALUES ('%s');", imagePath));
        }

        database.close();
    }

    public void insertPhoto() {
        adminOpenHelper = new AdminOpenHelper(context);
        SQLiteDatabase database = adminOpenHelper.getWritableDatabase();

        database.execSQL(String.format(Locale.getDefault(), "INSERT INTO PhotoGallery(path) VALUES ('%s');", imagePath));
        database.close();
    }

    public void deletePhoto(Uri photoUri) {
        adminOpenHelper = new AdminOpenHelper(context);
        SQLiteDatabase database = adminOpenHelper.getWritableDatabase();

        database.delete("PhotoGallery", "path =" + "'" + photoUri.toString() + "'", null);

        database.close();
    }

    public ArrayList<Uri> getHoleGallery() {
        adminOpenHelper = new AdminOpenHelper(context);
        SQLiteDatabase database = adminOpenHelper.getWritableDatabase();
        allImagesPath = new ArrayList<>();

        @SuppressLint("Recycle")
        Cursor cursor = database.rawQuery("SELECT * FROM PhotoGallery", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Uri imageUri = Uri.parse(cursor.getString(cursor.getColumnIndex("path")));
            allImagesPath.add(imageUri);
            cursor.moveToNext();
        }
        cursor.close();
        database.close();

        return allImagesPath;
    }


}
