package com.cookandroid.movie;




import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;


import com.cookandroid.movie.helper.FileManager;
import com.cookandroid.movie.helper.PermissionManager;


public class MainActivity extends AppCompatActivity {
    MenuItem mSearch;
    FileManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionManager pm = new PermissionManager(this, findViewById(R.id.MainView));;
        pm.checkPermission();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        fm = new FileManager(this, this);
        fm.refreshFiles();
    }

    public void mOnClick(View v) {
        switch (v.getId()) {
            case R.id.btnroot:
                if (fm.getmCurrent().compareTo(fm.getmRoot()) != 0) {
                    fm.setmCurrent(fm.getmRoot());
                    fm.refreshFiles();
                }
                break;
            case R.id.btnup:
                if (fm.getmCurrent().compareTo(fm.getmRoot()) != 0) {
                    int end = fm.getmCurrent().lastIndexOf("/");
                    String upPath = fm.getmCurrent().substring(0, end);
                    fm.setmCurrent(upPath);
                    fm.refreshFiles();
                }
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu, menu);
        mSearch = menu.findItem(R.id.search_icon);

        mSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener(){
            @Override
            public boolean onMenuItemActionExpand(MenuItem item){
                // 검색창 확장시 동작
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item){
                //검색창 축소시 동작
                return true;
            }
        });

        SearchView sv = (SearchView) mSearch.getActionView();
        sv.setSubmitButtonEnabled(true);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query){
                //검색버튼 클릭시 동작
                fm.setSearchText(query);
                fm.refreshFiles();
                // 키보드 닫기
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText){
                //타이핑 칠때마다 동작
                fm.setSearchText(newText);
                fm.refreshFiles();
                return true;
            }

        });

        return true;
    }
}
