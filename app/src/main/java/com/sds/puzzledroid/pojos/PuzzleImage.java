package com.sds.puzzledroid.pojos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.storage.StorageReference;
import com.sds.puzzledroid.GlideApp;
import com.sds.puzzledroid.activities.JigsawActivity;
import com.sds.puzzledroid.listeners.TouchListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class PuzzleImage {

    private Context context;
    private ImageView imageView;
    private Jigsaw jigsaw;
    private int localDifficulty;
    private final RelativeLayout layout;
    private final Context jigsawActivity;

    public PuzzleImage(Context context, ImageView imageView, Jigsaw jigsaw, int localDifficulty, RelativeLayout layout, JigsawActivity jigsawActivity) {
        this.context = context;
        this.imageView = imageView;
        this.jigsaw = jigsaw;
        this.localDifficulty = localDifficulty;
        this.layout = layout;
        this.jigsawActivity = jigsawActivity;
    }

    public Jigsaw getJigsaw() {
        return jigsaw;
    }

    // Selects an image from the Firebase Storage
    public StorageReference randomizeJigsawImage() {
        FBStorageImg fbStorageImg = new FBStorageImg();
        ArrayList<StorageReference> sRImages = fbStorageImg.getAllImgFiles();

        Random random = new Random();
        final int count = sRImages.size();
        int pos = random.nextInt(count);

        return sRImages.get(pos);
    }

    // Loads the selected image to JigsawActivity
    public void loadImage() {
        StorageReference img = randomizeJigsawImage();
        GlideApp.with(context)
                .load(img)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        imageView.post(new Runnable() {
                            @SuppressLint("ClickableViewAccessibility")
                            @Override
                            public void run() {
                                jigsaw = new Jigsaw(context.getApplicationContext(), imageView, localDifficulty);
                                TouchListener touchListener = new TouchListener(jigsawActivity);
                                //Shuffle pieces order
                                Collections.shuffle(jigsaw.getPieces());
                                for (PuzzlePiece piece : jigsaw.getPieces()) {
                                    piece.setOnTouchListener(touchListener);
                                    layout.addView(piece);
                                    // randomize position, on the bottom of the screen
                                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) piece.getLayoutParams();
                                    lParams.leftMargin = new Random().nextInt(layout.getWidth() - piece.pieceWidth);
                                    lParams.topMargin = layout.getHeight() - piece.pieceHeight - 50;
                                    piece.setLayoutParams(lParams);
                                }
                            }

                        });
                        return false;
                    }})
                .into(imageView);
    }
}
