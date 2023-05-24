package com.cookandroid.movie.helpers;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.cookandroid.movie.R;

public class OptionsMenuHelper {
    public static boolean onCreateOptionsMenu(Menu menu, FileManager fm, Context context){

        MenuInflater inflater = ((AppCompatActivity) context).getMenuInflater();
        inflater.inflate(R.menu.top_nav_menu, menu);
        //검색
        MenuItem mSearch = menu.findItem(R.id.search_icon);
        //정렬
        MenuItem mSort = menu.findItem(R.id.sort_title);
        MenuItem fCheckboxItem = menu.findItem(R.id.foldersort);
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
        mSort.setOnMenuItemClickListener(item -> {
            fm.isSorted();
            return true;
        });

        fCheckboxItem.setOnMenuItemClickListener(item -> {
          if (!item.isChecked()){
            fm.setChecked(true);
            fm.refreshFiles();
            fCheckboxItem.setChecked(true);
          }else{
            fm.setChecked(false);
            fm.refreshFiles();
            fCheckboxItem.setChecked(false);
          }
          return true;
        });

        SearchView sv = (SearchView) mSearch.getActionView();
        sv.setSubmitButtonEnabled(true);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query){
                //검색버튼 클릭시
                fm.setSearchText(query);
                fm.refreshFiles();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                // 키보드 닫기
                imm.hideSoftInputFromWindow(sv.getWindowToken(), 0);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText){
                //타이핑 칠때마다
                fm.setSearchText(newText);
                fm.refreshFiles();
                return true;
            }

        });
        return true;
    }




}
