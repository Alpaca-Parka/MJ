package com.example.test01;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 1;
    private ListView videoListView;
    private ArrayList<String> videoTitles;
    private ArrayAdapter<String> videoAdapter;

    private ArrayList<String> videoPaths;
    private ArrayList<Bitmap> videoThumbnails;


    ArrayList arFiles;
    String mRoot;
    String mCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoListView = findViewById(R.id.videoListView);
        videoTitles = new ArrayList<>();
        videoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videoTitles);
        videoListView.setAdapter(videoAdapter);
        videoPaths = new ArrayList<>();
        videoThumbnails = new ArrayList<>();

        arFiles = new ArrayList();

        mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        mCurrent = mRoot;

        videoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // 리스트 뷰 에 있는 영상 목록 클릭시 리스트 동작
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String selectedTitle = videoTitles.get(position); // 제목
                String selectedPath = Environment.getExternalStorageDirectory().getPath() + "/Download/" + selectedTitle;// 경로

                Intent intent = new Intent(MainActivity.this, VideoActivity.class); //intent 로 videoActivity 클래스로 가져옴
                intent.putExtra("text", selectedPath);// 경로
                startActivity(intent); // 실행
            }
        });

        if (checkPermissions()) {
            loadVideos();
        }
    }

    private boolean checkPermissions() { // 권한 확인
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
            return false;
        }
        return true;
    }

    private void loadVideos() { //String 으로 path 경로 받아옴
        String storagePath = Environment.getExternalStorageDirectory().getPath() + "/Download/";
        File storageDirectory = new File(storagePath);
        if (storageDirectory.exists() && storageDirectory.isDirectory()) {
            File[] files = storageDirectory.listFiles(); // 리스트 가져옴
            if (files != null) { // 남은 파일이 있는지 확인
                for (File file : files) { // 불러옴
                    if (file.isFile()) {
                        String title = file.getName();
                        videoTitles.add(title); //제목
                        videoPaths.add(file.getAbsolutePath());
                        videoThumbnails.add(getVideoThumbnail(file.getAbsolutePath()));
                    }
                }
                videoAdapter.notifyDataSetChanged();
            }
        }
    }





    private Bitmap getVideoThumbnail(String videoPath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        return retriever.getFrameAtTime();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadVideos();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}