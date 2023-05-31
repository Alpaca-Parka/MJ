package com.cookandroid.movie;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import com.cookandroid.movie.helper.PermissionManager;


public class MainActivity extends AppCompatActivity {
    String mCurrent;
    String mRoot;
    TextView mCurrentTxt;
    ListView mFileList;
    ArrayAdapter mAdapter;
    ArrayList arFiles;
    MenuItem mSearch; //검색
    MenuItem mSort; //정렬

    boolean mSortbtn; //정렬버튼 추가

    String searchText = ""; // 검색어를 저장할 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionManager pm = new PermissionManager(this, findViewById(R.id.MainView));

        pm.checkPermission();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        mCurrentTxt = (TextView)findViewById(R.id.current);
        mFileList = (ListView)findViewById(R.id.filelist);

        arFiles = new ArrayList();
        mRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
        mCurrent = mRoot;

        mAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, arFiles);
        mFileList.setAdapter(mAdapter);
        mFileList.setOnItemClickListener(mItemClickListener);


        refreshFiles();
    }
    AdapterView.OnItemClickListener mItemClickListener =
            new AdapterView.OnItemClickListener() {

                @SuppressLint("WrongConstant")
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    String name = (String)arFiles.get(position);

                    if(name.startsWith("[") && name.endsWith("]")){
                        name = name.substring(1, name.length() - 1);
                    }

                    String Path = mCurrent + "/" + name;
                    File f = new File(Path);
                    if(f.isDirectory()){
                        mCurrent = Path;
                        refreshFiles();
                    }else{
                        Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                        intent.putExtra("text",Path);
                        startActivity(intent);
                        refreshFiles();
                    }
                }
            };
    public void mOnClick(View v){
        switch(v.getId()){
            case R.id.btnroot:
                if(mCurrent.compareTo(mRoot) != 0){
                    mCurrent = mRoot;
                    refreshFiles();
                }
                break;
            case R.id.btnup:
                if(mCurrent.compareTo(mRoot) != 0){
                    int end = mCurrent.lastIndexOf("/");
                    String uppath = mCurrent.substring(0, end);
                    mCurrent = uppath;
                    refreshFiles();
                }
                break;
        }
    }
    //변경전 refreshFiles 오름차순

    void refreshFiles(){
        mCurrentTxt.setText(mCurrent);
        arFiles.clear();
        File current = new File(mCurrent);
        String[] files = current.list();



        if(files != null){
            for(int i = 0; i < files.length;i++){
                String Path = mCurrent + "/" + files[i];
                String name = "";

                File f = new File(Path);
                if(f.isDirectory()){
                    name = "[" + files[i] + "]";
                }else{
                    name = files[i];
                }
                                           //대소문자 구별하려면 toLowerCase() 사용하지 말 것
                if (searchText.isEmpty() || name.toLowerCase().contains(searchText)) { // 검색어가 비어있거나 파일 이름이 검색어를 포함하면 추가
                    arFiles.add(name);
                }
            }
        }


        if(mSortbtn) { //내림차순 정렬 desc
            Collections.sort(arFiles, Comparator.reverseOrder());
        }else { //오름차순 정렬 asc
            Collections.sort(arFiles);
        }



        mAdapter.notifyDataSetChanged();
    }


/*
    void refreshFiles() {
        mCurrentTxt.setText(mCurrent);
        ArrayList<String> sortedFiles = new ArrayList<>();
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

                if (searchText.isEmpty() || name.toLowerCase().contains(searchText)) {
                    sortedFiles.add(name);

                }

            }
        }

        // 파일 제목을 기준으로 정렬
        Collections.sort(sortedFiles);// sortedFiles 를 조건을 줘서 버튼 눌릴때마다 name에 저장된

        // 어댑터에 정렬된 리스트 설정
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, sortedFiles);
        mFileList.setAdapter(mAdapter);
    }
*/

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu, menu);

        mSearch = menu.findItem(R.id.search_icon);
        mSort = menu.findItem(R.id.sort_title); //mSort추가

        //검색
        mSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener(){
            @Override
            public boolean onMenuItemActionExpand(MenuItem item){
                // 검색창 확장시
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item){
                //검색창 축소시
                return true;
            }
        });

        //정렬
        mSort.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                mSortbtn = !mSortbtn; // 버튼 이 눌렸는지 체크
                refreshFiles();
                return true;
            }
        });



        SearchView sv = (SearchView) mSearch.getActionView();
        sv.setSubmitButtonEnabled(true);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query){
                //검색버튼 클릭시
                searchText = query;
                refreshFiles();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                // 키보드 닫기
                imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText){
                //타이핑 칠때마다
                searchText = newText;
                refreshFiles();
                return true;
            }

        });



        return true;
    }
}
