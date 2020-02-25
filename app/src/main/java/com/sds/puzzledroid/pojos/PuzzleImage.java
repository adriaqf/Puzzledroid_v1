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
import com.sds.puzzledroid.utils.FBStorageImg;

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

    // Selects an image from the Firebase Storage
    public StorageReference randomizeJigsawImage() {
        FBStorageImg fbStorageImg = new FBStorageImg();
        ArrayList<StorageReference> sRImages = fbStorageImg.getAllImgFiles();

        Random random = new Random();
        final int count = sRImages.size();
        int pos = random.nextInt(count);

        return sRImages.get(pos);
    }

}
