package com.sds.puzzledroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listview_item, null);
        }

        ImageView imageView = convertView.findViewById(R.id.imgTrophy);
        TextView tvSeconds = convertView.findViewById(R.id.tvSeconds);
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        imageView.setBackgroundResource(R.drawable.logo_app);
        tvSeconds.setText(String.valueOf(score.getTotalScore()));
        tvDate.setText(score.getDateTime());

        return convertView;
    }
}
