package com.sds.puzzledroid;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Integer> listPhoto = new ArrayList<Integer>();

    /** Constructor de clase */
    public ImageAdapter(Context c){
        mContext = c;
        //se cargan las miniaturas
        listPhoto.add(R.drawable.imagen1);
        listPhoto.add(R.drawable.imagen2);
        listPhoto.add(R.drawable.imagen3);
        listPhoto.add(R.drawable.imagen4);
        listPhoto.add(R.drawable.imagen5);
        listPhoto.add(R.drawable.imagen6);

    }

    @Override
    public int getCount() {
        return listPhoto.size();
    }

    @Override
    public Object getItem(int position) {
        return listPhoto.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewgroup) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageResource( listPhoto.get(position) );
        imageView.setScaleType( ImageView.ScaleType.CENTER_CROP );
        imageView.setLayoutParams( new GridView.LayoutParams(180, 220) );
        return imageView;
    }

}
