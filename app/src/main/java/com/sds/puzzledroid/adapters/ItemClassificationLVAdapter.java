package com.sds.puzzledroid.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.pojos.Score;

import java.util.ArrayList;
import java.util.Locale;

public class ItemClassificationLVAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Score> scoreArrayList;

    public ItemClassificationLVAdapter(Context context, ArrayList<Score> scoreArrayList) {
        this.context = context;
        this.scoreArrayList = scoreArrayList;
    }

    @Override
    public int getCount() {
        return scoreArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return scoreArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Score score = (Score) getItem(position);
        String scoreText = score.getTotalScore() + " " + context.getString(R.string.segundos2);
        /*String L8= Locale.getDefault().toString();
        switch(L8)
        {
            case "en_US":
                String scoreText = score.getTotalScore() + " SECONDS";
                break;
            case "fr_FR":
                String scoreText2 = score.getTotalScore() + " SECONDES";
                break;
            default:
                String scoreText3 = score.getTotalScore() + " SEGUNDOS";
        }*/

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
        }

        ImageView ivDifficulty = convertView.findViewById(R.id.iv_i_difficulty);
        Drawable ivdBackground = ivDifficulty.getBackground();
        TextView tvTrophy = convertView.findViewById(R.id.tvTrophy);
        TextView tvSeconds = convertView.findViewById(R.id.tvSeconds);
        TextView tvDate = convertView.findViewById(R.id.tvDate);


        //Including a difficulty's indicator to every list's row
        if(score.getDifficulty() == 0){
            ((GradientDrawable) ivdBackground).setColor(ContextCompat.getColor(convertView.getContext(), R.color.difficultyInd0));
        }
        else if(score.getDifficulty() == 1){
            ((GradientDrawable) ivdBackground).setColor(ContextCompat.getColor(convertView.getContext(), R.color.difficultyInd1));

        }
        else if(score.getDifficulty() == 2){
            ((GradientDrawable) ivdBackground).setColor(ContextCompat.getColor(convertView.getContext(), R.color.difficultyInd2));
        }

        //Showing trophies in first, second and third ranking place
        if(position == 0) {
            tvTrophy.setText("\uD83E\uDD47");
        }
        else if(position == 1) {
            tvTrophy.setText("\uD83E\uDD48");
        }
        else if(position == 2) {
            tvTrophy.setText("\uD83E\uDD49");
        }
        else {
            tvTrophy.setText(String.valueOf(position+1));
            tvTrophy.setTextSize(20);
        }

        /*String L9= Locale.getDefault().toString();
        switch(L8)
        {
            case "en_US":
                String scoreText = score.getTotalScore() + " SECONDS";
                tvSeconds.setText(scoreText);
                break;
            case "fr_FR":
                String scoreText2 = score.getTotalScore() + " SECONDES";
                tvSeconds.setText(scoreText2);
                break;
            default:
                String scoreText3 = score.getTotalScore() + " SEGUNDOS";
                tvSeconds.setText(scoreText3);
        }*/
        tvSeconds.setText(scoreText);
        tvDate.setText(score.getDateTime());

        return convertView;
    }
}
