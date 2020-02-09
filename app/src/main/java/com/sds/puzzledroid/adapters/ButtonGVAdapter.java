package com.sds.puzzledroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.activities.JigsawActivity;
import com.sds.puzzledroid.sqlite.SQLiteGalleryPhoto;

import java.io.IOException;
import java.util.ArrayList;


public class ButtonGVAdapter extends BaseAdapter {
    private Context mContext;
    private String[]  buttonsTitles = {
            "1",
            "2",
            "3"
    };

    public ButtonGVAdapter(Context c) {
        mContext = c;
    }

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final SQLiteGalleryPhoto sqLiteGalleryPhoto = new SQLiteGalleryPhoto(mContext);

        Button btn;
        String btnText;
        if (convertView == null) {
            btn = new Button(mContext);
            btn.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150));
            btn.setPadding(8, 8, 8, 8);
        }
        else {
            btn = (Button) convertView;
        }

        if(buttonsTitles[position].equals("1")) {
            btn.setBackgroundResource(R.drawable.button_single_player_level0);
            btnText = "FÁCIL";
        }
        else if(buttonsTitles[position].equals("2")) {
            btn.setBackgroundResource(R.drawable.button_single_player_level1);
            btnText = "MEDIO";
        }
        else {
            btn.setBackgroundResource(R.drawable.button_single_player_level2);
            btnText = "DIFÍCIL";
        }

        btn.setText(btnText);
        btn.setTextColor(Color.WHITE);
        btn.setTextSize((float) 15);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setId(position);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Uri> internalGalleryList = sqLiteGalleryPhoto.getHoleGallery();
                for(int i = 0; i < internalGalleryList.size(); i++) {
                    System.out.println(internalGalleryList.get(i).toString());
                }
                if(internalGalleryList.isEmpty()) {
                    Toast.makeText(mContext, "No hay fotos disponibles. Añade una foto para seguir jugando.", Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(v.getContext(), JigsawActivity.class);
                    //0 = easy, 1 = normal, 2 = difficult
                    int difficulty = buttonsTitles[position].equals("1")  ? 0 :
                            buttonsTitles[position].equals("2") ? 1 : 2;
                    i.putExtra("levelDifficulty", difficulty);
                    v.getContext().startActivity(i);
                }
            }
        });

        return btn;
    }

}
