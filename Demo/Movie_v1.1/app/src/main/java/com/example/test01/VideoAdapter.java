package com.example.test01;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class VideoAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> videoTitles;
    private ArrayList<String> videoPaths;
    private ArrayList<Bitmap> videoThumbnails;

    public VideoAdapter(Context context, ArrayList<String> videoTitles, ArrayList<String> videoPaths, ArrayList<Bitmap> videoThumbnails) {
        super(context, R.layout.list_item_video, videoTitles);
        this.context = context;
        this.videoTitles = videoTitles;
        this.videoPaths = videoPaths;
        this.videoThumbnails = videoThumbnails;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item_video, parent, false);

        ImageView thumbnailImageView = view.findViewById(R.id.thumbnailImageView);
        TextView titleTextView = view.findViewById(R.id.titleTextView);

        Bitmap thumbnail = videoThumbnails.get(position);
        if (thumbnail != null) {
            thumbnailImageView.setImageBitmap(thumbnail);
        } else {
            thumbnailImageView.setImageResource(R.drawable.placeholder_image); // 썸네일이 없는 경우 기본 이미지를 표시
        }

        titleTextView.setText(videoTitles.get(position));

        return view;
    }
}

