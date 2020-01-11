package com.sds.puzzledroid.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.sds.puzzledroid.R;
import com.sds.puzzledroid.logic.Score;

import java.util.ArrayList;

public class ItemLVAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Score> scoreArrayList;
    private View convertView;

    public ItemLVAdapter(Context context, ArrayList<Score> scoreArrayList) {
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Score score = (Score) getItem(position);
        String scoreText = score.getTotalScore() + " SEGUNDOS";

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
        }

        ImageView ivDifficulty = convertView.findViewById(R.id.iv_i_difficulty);
        Drawable ivdBackground = ivDifficulty.getBackground();

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
        TextView tvTrophy = convertView.findViewById(R.id.tvTrophy);
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

        TextView tvSeconds = convertView.findViewById(R.id.tvSeconds);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        tvSeconds.setText(scoreText);
        tvDate.setText(score.getDateTime());

        return convertView;
    }
}
