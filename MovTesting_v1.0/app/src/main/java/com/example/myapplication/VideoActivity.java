package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;


public class VideoActivity extends AppCompatActivity {
    VideoView vv;
    PlayerView pv;
    SimpleExoPlayer player;
    PlayerControlView pvc;
    String Path;
    Uri videoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_exo);

        Intent intent = getIntent();
        Path = intent.getStringExtra("text");
        videoUri = Uri.parse(Path);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pv = findViewById(R.id.pv);
        pvc = findViewById(R.id.pvc);
        /*
        vv= findViewById(R.id.vv);
        vv.setMediaController(new MediaController(this));
        //VideoView가 보여줄 동영상의 경로 주소(Uri) 설정하기
        vv.setVideoPath(Path);
        vv.start();*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        player = new SimpleExoPlayer.Builder(this.getApplicationContext()).build();
        pv.setPlayer(player);
        pvc.setPlayer(player);

        DataSource.Factory factory = new DefaultDataSourceFactory(this, "Ex89VideoAndExoPlayer");
        ProgressiveMediaSource mediaSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(videoUri);

        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
        Log.d("123",Path);
    }

    @Override
    protected void onStop() {
        super.onStop();

        pv.setPlayer(null);
        player.release();
        player=null;
    }
}