package com.cookandroid.movie.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.cookandroid.movie.MainActivity;
import com.cookandroid.movie.R;
import com.cookandroid.movie.VideoActivity;

import java.io.File;
import java.util.ArrayList;

public class FileManager {
    private final AppCompatActivity activity;
    private String mCurrent;
    private final String mRoot;
    private String searchText = "";
    private final TextView mCurrentTxt;
    private ListView mFileList;
    private ArrayAdapter mAdapter;
    private ArrayList arFiles;
    private MainActivity ma;


    public FileManager(AppCompatActivity _activity, Context _context) {
        this.activity = _activity;

        mCurrentTxt = (TextView) activity.findViewById(R.id.current);
        mFileList = (ListView) activity.findViewById(R.id.filelist);

        arFiles = new ArrayList();
        mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        mCurrent = mRoot;

        mAdapter = new ArrayAdapter(_context, android.R.layout.simple_list_item_1, arFiles);
        mFileList.setAdapter(mAdapter);
        mFileList.setOnItemClickListener(mItemClickListener);
    }

    public void refreshFiles() {
        mCurrentTxt.setText(mCurrent);
        arFiles.clear();
        File current = new File(mCurrent);
        String[] files = current.list();

        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String Path = mCurrent + "/" + files[i];
                String name = "";

                File f = new File(Path);
                if (f.isDirectory()) {
                    name = "[" + files[i] + "]";
                } else {
                    name = files[i];
                }
                //대소문자 구별하려면 toLowerCase() 사용하지 말 것
                if (searchText.isEmpty() || name.toLowerCase().contains(searchText)) { // 검색어가 비어있거나 파일 이름이 검색어를 포함하면 추가
                    arFiles.add(name);
                }
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            String name = (String) arFiles.get(position);

            if (name.startsWith("[") && name.endsWith("]")) {
                name = name.substring(1, name.length() - 1);
            }

            String Path = mCurrent + "/" + name;
            File f = new File(Path);
            if (f.isDirectory()) {
                mCurrent = Path;
                refreshFiles();
            } else {
                Intent intent = new Intent(activity, VideoActivity.class);
                intent.putExtra("text", Path);
                activity.startActivity(intent);
                refreshFiles();
            }
        }
    };
    public void upRoot(){
        if (mCurrent.compareTo(mRoot) != 0) {
            mCurrent = mRoot;
            refreshFiles();
        }
    }
    public void updir() {
        if (mCurrent.compareTo(mRoot) != 0) {
            int end = mCurrent.lastIndexOf("/");
//            String upPath = mCurrent.substring(0, end);
//            mCurrent = upPath;
            mCurrent = mCurrent.substring(0, end);
            refreshFiles();
        }
    }

    public String getmCurrent() {
        return mCurrent;
    }

    public String getmRoot() {
        return mRoot;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }
}
