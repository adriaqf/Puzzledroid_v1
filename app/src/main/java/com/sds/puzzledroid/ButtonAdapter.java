package com.sds.puzzledroid;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;


public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    private String[]  buttonsTitles = {
            "1",
            "2",
            "3",
            "4",
            "5"
    };
    private AssetManager am;
    private String[] files;

    public ButtonAdapter(Context c) {
        mContext = c;
        am = mContext.getAssets();
        try {
            files  = am.list("img");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /** Constructor de clase */


    @Override
    public int getCount() {
        return buttonsTitles.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewgroup) {

        Button btn;
        if (view == null) {
            // if it's not recycled, initialize some attributes-ñ
            btn = new Button(mContext);
            btn.setLayoutParams(new GridView.LayoutParams(200, 200));
            btn.setPadding(8, 8, 8, 8);
        }
        else {
            btn = (Button) view;
        }

        if(buttonsTitles[position] == "1" || buttonsTitles[position] == "2") {
            btn.setBackgroundResource(R.drawable.button_single_player_level0);
        }
        else if(buttonsTitles[position] == "3" || buttonsTitles[position] == "4") {
            btn.setBackgroundResource(R.drawable.button_single_player_level1);
        }
        else {
            btn.setBackgroundResource(R.drawable.button_single_player_level2);
        }

        btn.setText(buttonsTitles[position]);
        btn.setTextColor(Color.WHITE);
        btn.setTextSize((float) 20);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setId(position);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), PuzzleLevelActivity.class);
                v.getContext().startActivity(i);
                Toast.makeText(v.getContext(), "Hello World", Toast.LENGTH_SHORT).show();
            }
        });

        return btn;
    }
    private Bitmap getPicFromAsset(ImageView imageView, String assetName) {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        if(targetW == 0 || targetH == 0) {
            // view has no dimensions set
            return null;
        }

        try {
            InputStream is = am.open("img/" + assetName);
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

            return BitmapFactory.decodeStream(is, new Rect(-1, -1, -1, -1), bmOptions);
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }


}
