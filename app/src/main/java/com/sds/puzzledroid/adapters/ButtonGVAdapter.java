package com.sds.puzzledroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.activities.JigsawActivity;
import java.util.ArrayList;
import java.util.Locale;



public class ButtonGVAdapter extends BaseAdapter {
    private Context mContext;
    private SoundPool sp;
    private int sound_clic;

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
        sp = new SoundPool(1, AudioManager.STREAM_MUSIC,1);
        sound_clic = sp.load(mContext,R.raw.clic,1);

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
            btnText= mContext.getString(R.string.facil);
        }
        else if(buttonsTitles[position].equals("2")) {
            btn.setBackgroundResource(R.drawable.button_single_player_level1);
            btnText= mContext.getString(R.string.medio);
        }
        else {
            btn.setBackgroundResource(R.drawable.button_single_player_level2);
            btnText= mContext.getString(R.string.dificil);
        }

        btn.setText(btnText);
        btn.setTextColor(Color.WHITE);
        btn.setTextSize((float) 15);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setId(position);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), JigsawActivity.class);
                    //0 = easy, 1 = normal, 2 = difficult
                    SoundPool();
                    vibrate();
                    int difficulty = buttonsTitles[position].equals("1")  ? 0 :
                            buttonsTitles[position].equals("2") ? 1 : 2;
                    i.putExtra("levelDifficulty", difficulty);
                    v.getContext().startActivity(i);
                }
        });

        return btn;
    }

    private void vibrate(){
        SharedPreferences pref = mContext.getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        boolean vibrate = pref.getBoolean("sw_vibrate",true);
        Vibrator vibrator=(Vibrator)mContext.getApplicationContext() .getSystemService(Context.VIBRATOR_SERVICE);
        if(vibrate){
            vibrator.vibrate(50);
        }
    }

    private void SoundPool () {
        SharedPreferences pref = mContext.getSharedPreferences("GlobalSettings",Context.MODE_PRIVATE);
        boolean value = pref.getBoolean("effects_sound",true);
        if(value){
            sp.play(sound_clic,1,1,1,0,0);
        }
    }
}
