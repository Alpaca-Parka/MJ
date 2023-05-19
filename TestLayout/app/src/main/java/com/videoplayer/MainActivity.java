package com.videoplayer;

import static com.videoplayer.R.id.listview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItem;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Menu menu;
    ArrayList<SampleData> movieDataList;
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
        movieDataList = new ArrayList<SampleData>();

        movieDataList.add(new SampleData(R.drawable.a, "아 뭐하지","4min"));
        movieDataList.add(new SampleData(R.drawable.b, "하기시러","18min"));
    }





}