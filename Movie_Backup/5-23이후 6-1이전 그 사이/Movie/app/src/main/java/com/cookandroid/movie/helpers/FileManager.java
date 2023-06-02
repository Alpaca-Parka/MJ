package com.cookandroid.movie.helpers;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cookandroid.movie.R;
import com.cookandroid.movie.VideoActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FileManager {
    private final AppCompatActivity activity;
    private String mCurrent;
    private final String mRoot;
    private String searchText = "";
    private final TextView mCurrentTxt;
    private final ArrayAdapter<String> mAdapter;
    //private ArrayList arFiles;
    private final ArrayList<String> sortedFiles;
    private boolean isSort = false;
    private boolean isChecked = false;
    Comparator<String> caseInsensitiveComparator = String.CASE_INSENSITIVE_ORDER;
    Comparator<String> reverseComparator = Collections.reverseOrder(caseInsensitiveComparator);

    public FileManager(AppCompatActivity _activity, Context _context) {
        this.activity = _activity;

        mCurrentTxt = (TextView) activity.findViewById(R.id.current);
        ListView mFileList = (ListView) activity.findViewById(R.id.filelist);

        sortedFiles = new ArrayList<>();
        mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        mCurrent = mRoot;

        mAdapter = new ArrayAdapter<>(_context, android.R.layout.simple_list_item_1, sortedFiles);
        mFileList.setAdapter(mAdapter);


        AdapterView.OnItemClickListener mItemClickListener = (parent, view, position, id) -> {
            String name = (String) sortedFiles.get(position);//현재 디렉토리의 위치를 가져옴

            if (name.startsWith("[") && name.endsWith("]")) {
                name = name.substring(1, name.length() - 1);
            }

            String Path = mCurrent + "/" + name;
            File f = new File(Path);
            if (f.isDirectory()) {
                //디렉토리일 경우 동작
                mCurrent = Path;
                refreshFiles();
            } else {
                //파일일 경우 동작
                Intent intent = new Intent(activity, VideoActivity.class);
                intent.putExtra("text", Path);
                activity.startActivity(intent);
                refreshFiles();
            }
        };
        mFileList.setOnItemClickListener(mItemClickListener);
    }

    public void refreshFiles() {
        mCurrentTxt.setText(mCurrent);
        sortedFiles.clear();
        isSort = false;//초기화 해야 처음에 이동하면 순차정렬

        File current = new File(mCurrent);
        String[] files = current.list();

        if (files != null) {
            for (String file : files) {
                String Path = mCurrent + "/" + file;
                String name;

                File f = new File(Path);
                if (f.isDirectory()) {
                    name = "[" + file + "]";
                } else {
                    name = file;
                }
//
                if (searchText.isEmpty() || name.toLowerCase().contains(searchText)) {
                    // 검색어가 비어있거나 파일 이름이 검색어를 포함하면 추가
                    sortedFiles.add(name);
                }
            }
        }

        isSorted();//정렬확인
    }

    public void upRoot() {
        //루트로 이동
        if (mCurrent.compareTo(mRoot) != 0) {
            mCurrent = mRoot;
            refreshFiles();
        }
    }

    public void updir() {
        //상위 디렉토리로 이동
        if (mCurrent.compareTo(mRoot) != 0) {
            int end = mCurrent.lastIndexOf("/");
            mCurrent = mCurrent.substring(0, end);
            refreshFiles();
        }
    }

    public void isSorted() {
        //정렬이 되었는지 안되었는지 판단해서 동작

        if (isSort) {
            //순차정렬 -> 역순정렬
            if (isChecked) {
                //체크하면 폴더 우선 정렬
                sortedFiles.sort((str1, str2) -> {
                    String lowerStr1 = str1.toLowerCase();
                    String lowerStr2 = str2.toLowerCase();

                    if (lowerStr1.startsWith("[") && !lowerStr2.startsWith("[")) {
                        return -1;
                    } else if (!lowerStr1.startsWith("[") && lowerStr2.startsWith("[")) {
                        return 1;
                    } else {
                        return lowerStr1.compareToIgnoreCase(lowerStr2);
                    }
                });
                Collections.reverse(sortedFiles);
            } else {
                //체크를 안하면 기본정렬
                sortedFiles.sort(reverseComparator);
            }
            isSort = false;
        } else {
//            역순정렬 -> 순차정렬
            if (isChecked) {
                //체크하면 폴더 우선 정렬
                sortedFiles.sort((str1, str2) -> {
                    String lowerStr1 = str1.toLowerCase();
                    String lowerStr2 = str2.toLowerCase();

                    if (lowerStr1.startsWith("[") && !lowerStr2.startsWith("[")) {
                        return -1;
                    } else if (!lowerStr1.startsWith("[") && lowerStr2.startsWith("[")) {
                        return 1;
                    } else {
                        return lowerStr1.compareToIgnoreCase(lowerStr2);
                    }
                });
            } else {
                //체크를 안하면 기본정렬
                sortedFiles.sort(caseInsensitiveComparator);
            }
            isSort = true;
        }
        mAdapter.notifyDataSetChanged();
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

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}