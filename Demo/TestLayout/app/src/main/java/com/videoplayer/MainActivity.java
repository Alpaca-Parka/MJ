package com.videoplayer;

import static com.videoplayer.R.id.listview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Menu menu;
    String VideoTitle;
    private ArrayList<Bitmap> bmThumbnail;

    ArrayList<SampleData> movieDataList;


    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download";
    File directory = new File(path);
    File[] files = directory.listFiles();


    List<String> filesNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.InitializeMovieData();

        ListView listview = (ListView)findViewById(R.id.listview);
        final MyAdapter myAdapter = new MyAdapter(this,movieDataList);

        listview.setAdapter(myAdapter);


    }
    public void InitializeMovieData()
    {

        for (int i=0; i< files.length; i++) {
            filesNameList.add(files[i].getName());
        }

        movieDataList = new ArrayList<SampleData>();

        movieDataList.add(new SampleData(R.drawable.a, filesNameList.get(0).toString(),"4min"));
        movieDataList.add(new SampleData(R.drawable.b, "하기시러","18min"));
    }





}