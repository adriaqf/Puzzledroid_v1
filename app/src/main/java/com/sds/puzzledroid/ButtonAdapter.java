package com.sds.puzzledroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;


public class ButtonAdapter extends BaseAdapter {
    private Context mContext;
    private String[]  buttonsTitles = {
            "1",
            "2",
            "3",
            "4",
            "5"
    };

    /** Constructor de clase */
    public ButtonAdapter(Context c){
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
        return position;
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

}
