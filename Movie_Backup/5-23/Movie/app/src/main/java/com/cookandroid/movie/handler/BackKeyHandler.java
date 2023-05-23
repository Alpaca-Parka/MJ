package com.cookandroid.movie.handler;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.cookandroid.movie.R;
import com.cookandroid.movie.helper.FileManager;

public class BackKeyHandler {
    private Activity activity;
    private Toast toast;
    private long backKeyPressedTime = 0;

    public BackKeyHandler(Activity _activity) {
        this.activity = _activity;
    }

    private void showGuide(){
        toast = Toast.makeText(activity,"\'뒤로가기\' 키를 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            activity.finish();
            toast.cancel();
        }
    }
}
