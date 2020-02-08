package com.sds.puzzledroid.logic;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.sds.puzzledroid.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class Jigsaw {
    private Context context;
    private ArrayList<PuzzlePiece> pieces;
    private int levelDifficulty;



    public Jigsaw(Context context, String assetImageName, ImageView actImageView, int levelDifficulty){
        this.context = context;
        this.levelDifficulty = levelDifficulty;

        if (assetImageName != null) {
            setPicFromAsset(assetImageName, actImageView);
        }
        pieces = splitJigsawImage(actImageView);

    }

    public ArrayList<PuzzlePiece> getPieces() {
        return this.pieces;
    }

    private void setPicFromAsset(String assetImageName, ImageView actImageView) {
        // Get the dimensions of the activity_jigsaw's ImageView
        int targetW = actImageView.getWidth();
        int targetH = actImageView.getHeight();

        AssetManager am = context.getAssets();

        try {
            InputStream is = am.open("img/" + assetImageName);

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            is.reset();

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), bmOptions);
            actImageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ArrayList<PuzzlePiece> splitJigsawImage(ImageView actImageView) {

        //Selecting difficulty of the level
        int piecesNumber = levelDifficulty == 0 ? 4 : levelDifficulty == 1 ? 9 : 16;
        int rows = piecesNumber == 4 ? 2 : piecesNumber == 9 ? 3 : 4;
        int cols = piecesNumber == 4 ? 2 : piecesNumber == 9 ? 3 : 4;

        ArrayList<PuzzlePiece> pieces = new ArrayList<>(piecesNumber);

        // Get the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) actImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        int[] dimensions = getBitmapPositionInsideImageView(actImageView);
        int scaledBitmapLeft = dimensions[0];
        int scaledBitmapTop = dimensions[1];
        int scaledBitmapWidth = dimensions[2];
        int scaledBitmapHeight = dimensions[3];

        int croppedImageWidth = scaledBitmapWidth - 2 * abs(scaledBitmapLeft);
        int croppedImageHeight = scaledBitmapHeight - 2 * abs(scaledBitmapTop);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledBitmapWidth, scaledBitmapHeight, true);
        Bitmap croppedBitmap = Bitmap.createBitmap(scaledBitmap, abs(scaledBitmapLeft), abs(scaledBitmapTop), croppedImageWidth, croppedImageHeight);

        // Calculate the with and height of the pieces
        int pieceWidth = croppedImageWidth/cols;
        int pieceHeight = croppedImageHeight/rows;

        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                Bitmap pieceBitmap = Bitmap.createBitmap(croppedBitmap, xCoord, yCoord, pieceWidth, pieceHeight);
                PuzzlePiece piece = new PuzzlePiece(context.getApplicationContext());
                piece.setImageBitmap(pieceBitmap);
                piece.xCoord = xCoord + actImageView.getLeft();
                piece.yCoord = yCoord + actImageView.getTop();
                piece.pieceWidth = pieceWidth;
                piece.pieceHeight = pieceHeight;

                pieces.add(piece);

                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }

        return pieces;
    }

    private int[] getBitmapPositionInsideImageView(ImageView actImageView) {
        int[] sBmDimensions = new int[4];

        if (actImageView == null || actImageView.getDrawable() == null) {
            return sBmDimensions;
        }

        // Get image dimensions
        // Get image matrix values and place them in an array
        float[] f = new float[9];
        actImageView.getImageMatrix().getValues(f);

        // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
        final float scaleX = f[Matrix.MSCALE_X];
        final float scaleY = f[Matrix.MSCALE_Y];

        // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
        final Drawable d = actImageView.getDrawable();
        final int origW = d.getIntrinsicWidth();
        final int origH = d.getIntrinsicHeight();

        // Calculate the actual dimensions
        final int actW = Math.round(origW * scaleX);
        final int actH = Math.round(origH * scaleY);

        sBmDimensions[2] = actW;
        sBmDimensions[3] = actH;

        // Get image position
        // We assume that the image is centered into ImageView
        int imgViewW = actImageView.getWidth();
        int imgViewH = actImageView.getHeight();

        int top = (imgViewH - actH)/2;
        int left = (imgViewW - actW)/2;

        sBmDimensions[0] = left;
        sBmDimensions[1] = top;

        return sBmDimensions;
    }

    public boolean isJigsawCompleted() {
        for (PuzzlePiece piece : pieces) {
            if (piece.canMove) {
                return false;
            }
        }

        return true;
    }


}
