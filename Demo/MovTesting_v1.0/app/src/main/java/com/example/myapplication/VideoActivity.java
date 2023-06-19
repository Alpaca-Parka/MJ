package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class VideoActivity extends AppCompatActivity {
    VideoView vv;
    String Path;
    Uri videoUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_exo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Path = intent.getStringExtra("text");
        videoUri = Uri.parse(Path);

        //Path = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4";
        //videoUri = Uri.parse(Path);

        Log.d("test(Path)", ":"+Path);
        Log.d("test(Uri)", ":"+videoUri);


        //videoview 예제
        /*
        vv= findViewById(R.id.vv);
        vv.setMediaController(new MediaController(this));
        //VideoView가 보여줄 동영상의 경로 주소(Uri) 설정하기
        vv.setVideoPath(Path);
        vv.start();*/

        //exoPlayer
        PlayerView playerView = findViewById(R.id.pv);

        SimpleExoPlayer simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(simpleExoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(videoUri);

        String userAgent = Util.getUserAgent(this,getApplicationInfo().name);
        DefaultDataSourceFactory factory = new DefaultDataSourceFactory(this, userAgent);
        ProgressiveMediaSource progressiveMediaSource = new ProgressiveMediaSource.Factory(factory).createMediaSource(mediaItem);

        simpleExoPlayer.setMediaSource(progressiveMediaSource);

        simpleExoPlayer.prepare();

        simpleExoPlayer.play();

    }
}