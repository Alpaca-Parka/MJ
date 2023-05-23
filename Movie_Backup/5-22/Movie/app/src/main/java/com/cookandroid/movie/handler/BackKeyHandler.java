package com.cookandroid.movie.handler;

import android.app.Activity;
import android.widget.Toast;

public class BackKeyHandler {
    private Activity activity;
    private Toast toast;
    private long backKeyPressedTime = 0;

    public BackKeyHandler(Activity _activity) {
        this.activity = _activity;
    }

    private void showGuide(){
        toast = Toast.makeText(activity,"\'뒤로가기\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
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