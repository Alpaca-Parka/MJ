package com.cookandroid.movie;



import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cookandroid.movie.helpers.DoubleBackPressHandler;
import com.cookandroid.movie.helpers.FileManager;
import com.cookandroid.movie.helpers.OptionsMenuHelper;
import com.cookandroid.movie.helpers.PermissionManager;

public class MainActivity extends AppCompatActivity {
    private FileManager fm;
    private final DoubleBackPressHandler doubleBackPressHandler = new DoubleBackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //권한체크
        PermissionManager pm = new PermissionManager(this, findViewById(R.id.MainView));
        pm.checkPermission();

        //툴바(액션바) 생성
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        //파일 매니저 생성
        fm = new FileManager(this,this);
        fm.refreshFiles();
    }

    public void mOnClick(View v){
        int id = v.getId();
        if (id == R.id.btnroot){
            fm.upRoot();
        }else if (id == R.id.btnup){
            fm.updir();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return OptionsMenuHelper.onCreateOptionsMenu(menu, fm, this);
    }

    @Override
    public void onBackPressed() {//뒤로가기 키 동작
        if(fm.getmCurrent().compareTo(fm.getmRoot())!=0) {
            //루트가 아니면 상위 디렉토리로 이동
            fm.updir();
        }else{
            //루트면 앱종료
            doubleBackPressHandler.onBackPressed();
        }
    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return OptionsMenuHelper.onOptionsItemSelected(this, item, fm)
                || super.onOptionsItemSelected(item);
    }
}
