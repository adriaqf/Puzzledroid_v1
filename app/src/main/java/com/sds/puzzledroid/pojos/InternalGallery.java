package com.sds.puzzledroid.pojos;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.sds.puzzledroid.sqlite.SQLiteGalleryPhoto;

import java.util.ArrayList;

public class InternalGallery {

    private Context context;
    private final ArrayList<Uri> allImagesPath = new ArrayList<>();

    public InternalGallery(Context context) {
        this.context = context;
    }

    public void saveFullGallery() {
        String[] projection = new String[]{MediaStore.Images.Media._ID};
        Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uriExternal, projection, null, null, null);

        if(cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            while(cursor.moveToNext()) {
                Uri uriImage = Uri.withAppendedPath(uriExternal, cursor.getString(columnIndex) + "");
                allImagesPath.add(uriImage);
            }
            cursor.close();
        }

        //Inserting the results to the data base
        SQLiteGalleryPhoto galleryPhoto = new SQLiteGalleryPhoto(context, allImagesPath);
        galleryPhoto.insertHoleGallery();
    }
}
