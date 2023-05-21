package com.videoplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<SampleData> sample;

    public MyAdapter(Context context, ArrayList<SampleData> data) {
        mContext = context;
        sample = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return sample.size();
    }

    @Override
    public SampleData getItem(int position) {
        return sample.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.listview, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.Thumnail);
        TextView movieName = (TextView)view.findViewById(R.id.VideoTitle);
        TextView grade = (TextView)view.findViewById(R.id.VideoTime);

        imageView.setImageResource(sample.get(position).getThumnail());
        movieName.setText(sample.get(position).getVideoTitle());
        grade.setText(sample.get(position).getVideoTime());

        return view;
    }
}
