package com.sds.puzzledroid.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import com.sds.puzzledroid.sqlite.SQLiteGalleryPhoto;

import java.util.ArrayList;
import java.util.Random;

public class ImagePuzzle {

    private Context context;
    private ImageView puzzleImage;
    private Bitmap currentBitmap = null;

    public ImagePuzzle(Context context, ImageView puzzleImage) {
        this.context = context;
        this.puzzleImage = puzzleImage;
    }

    public Uri randomizeJigsawImage() {
        SQLiteGalleryPhoto sqLiteGalleryPhoto = new SQLiteGalleryPhoto(context);
        ArrayList<Uri> allImagesPath = sqLiteGalleryPhoto.getHoleGallery();

        final Random random = new Random();
        final int count = allImagesPath.size();
        int num = random.nextInt(count);

        String imagePath = allImagesPath.get(num).toString();
        System.out.println("Random: " + imagePath);
        if(currentBitmap != null) {
            currentBitmap.recycle();
            currentBitmap = BitmapFactory.decodeFile(imagePath);
        }
        Uri uImagePath = Uri.parse(imagePath);
        puzzleImage.setImageURI(uImagePath);

        return uImagePath;
    }


}
