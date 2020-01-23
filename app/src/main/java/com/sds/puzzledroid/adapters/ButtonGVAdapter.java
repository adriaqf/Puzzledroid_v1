package com.sds.puzzledroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.activities.JigsawActivity;

import java.io.IOException;


public class ButtonGVAdapter extends BaseAdapter {
    private Context mContext;
    private String[]  buttonsTitles = {
            "1",
            "2",
            "3",
            "4",
            "5"
    };
    private String[] files;

    public ButtonGVAdapter(Context c) {
        mContext = c;
        AssetManager am = mContext.getAssets();
        try {
            files  = am.list("img");
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        Button btn;
        if (convertView == null) {
            btn = new Button(mContext);
            btn.setLayoutParams(new GridView.LayoutParams(200, 200));
            btn.setPadding(8, 8, 8, 8);
        }
        else {
            btn = (Button) convertView;
        }

        if(buttonsTitles[position].equals("1") || buttonsTitles[position].equals("2")) {
            btn.setBackgroundResource(R.drawable.button_single_player_level0);
        }
        else if(buttonsTitles[position].equals("3") || buttonsTitles[position].equals("4")) {
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
                Intent i = new Intent(v.getContext(), JigsawActivity.class);
                //0 = easy, 1 = normal, 2 = difficult
                int difficulty = buttonsTitles[position].equals("1") || buttonsTitles[position].equals("2") ? 0 :
                        buttonsTitles[position].equals("3") || buttonsTitles[position].equals("4") ? 1 : 2;
                i.putExtra("levelDifficulty", difficulty);
                i.putExtra("assetName", files[position]);
                v.getContext().startActivity(i);
            }
        });

        return btn;
    }

}
