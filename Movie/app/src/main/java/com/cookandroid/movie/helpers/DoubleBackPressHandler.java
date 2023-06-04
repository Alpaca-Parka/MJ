package com.cookandroid.movie.helpers;

import android.app.Activity;
import android.widget.Toast;

public class DoubleBackPressHandler {
    private final Activity activity;
    private Toast toast;
    private long backKeyPressedTime = 0;

    /** 생성자 */
    public DoubleBackPressHandler(Activity _activity) {
        this.activity = _activity;
    }

    /**뒤로가기키 1번 눌렀을때 동작할 내용*/
    private void showGuide(){
        toast = Toast.makeText(activity, "'뒤로가기' 키를 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    /**뒤로가기키 동작 처리*/
    public void onBackPressed(){
        if(System.currentTimeMillis() > backKeyPressedTime + 2000){
            //뒤로가기키 2번누른게 2초이내가 아니면 동작
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000){
            //뒤로가기키 2번누른게 2초이내면 동작
            activity.finish();
            toast.cancel();
        }
    }
}
