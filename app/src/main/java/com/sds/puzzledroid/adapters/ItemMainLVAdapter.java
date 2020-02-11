package com.sds.puzzledroid.adapters;

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
import com.sds.puzzledroid.pojos.LCalendarEvent;

import java.util.ArrayList;

public class ItemMainLVAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LCalendarEvent> events;

    public ItemMainLVAdapter(Context context, ArrayList<LCalendarEvent> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LCalendarEvent event = (LCalendarEvent) getItem(position);
        String scoreText = event.getTitle() + " SEGUNDOS";

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
        }

        ImageView ivDifficulty = convertView.findViewById(R.id.iv_i_difficulty);
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) ivDifficulty.getLayoutParams();
        params.width = 30;
        params.height = 30;
        ivDifficulty.setLayoutParams(params);

        Drawable ivdBackground = ivDifficulty.getBackground();
        TextView tvTrophy = convertView.findViewById(R.id.tvTrophy);
        TextView tvSeconds = convertView.findViewById(R.id.tvSeconds);
        TextView tvDate = convertView.findViewById(R.id.tvDate);


        //Including a difficulty's indicator to every list's row
        if(event.getDescription().equals("Dificultad: Fácil")){
            ((GradientDrawable) ivdBackground).setColor(ContextCompat.getColor(convertView.getContext(), R.color.difficultyInd0));
        }
        else if(event.getDescription().equals("Dificultad: Media")){
            ((GradientDrawable) ivdBackground).setColor(ContextCompat.getColor(convertView.getContext(), R.color.difficultyInd1));

        }
        else if(event.getDescription().equals("Dificultad: Difícil")){
            ((GradientDrawable) ivdBackground).setColor(ContextCompat.getColor(convertView.getContext(), R.color.difficultyInd2));
        }

        tvDate.setVisibility(View.GONE);
        tvTrophy.setText("▪️");
        tvTrophy.setTextSize(20);
        tvSeconds.setText(scoreText);
        tvSeconds.setTextSize(15);

        return convertView;
    }
}
