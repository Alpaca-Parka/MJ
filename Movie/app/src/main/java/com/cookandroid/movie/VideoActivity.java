package com.cookandroid.movie;

import android.content.Intent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

import com.google.android.exoplayer2.ui.PlayerView;
public class VideoActivity extends AppCompatActivity {

    private String videoPath;
    private SimpleExoPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_exo); // exo 플레이어 레이아웃

        Intent intent = getIntent(); //intent 가져옴
        if (intent != null) {
            videoPath = intent.getStringExtra("text");
        }

        PlayerView playerView = findViewById(R.id.pv); // 엑소플레이어 pv 로 선언해둔것

        player = new SimpleExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(videoPath); //저장 위치
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}