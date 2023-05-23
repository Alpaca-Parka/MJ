package com.cookandroid.movie.helper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cookandroid.movie.MainActivity;
import com.cookandroid.movie.PermissionActivity;
import com.cookandroid.movie.handler.BackKeyHandler;
import com.google.android.material.snackbar.Snackbar;

public class PermissionManager {
    /**
     * TED Permission 사용하면 간편하게 권한 부여 가능
     * */
    private SharedPreferences pref;
    private static final int REQ_CODE = 1;
    private final AppCompatActivity activity;
    private final View view;
    private final String[] permissions = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            ? new String[]{//api 33 이상이면 이러한 권한 부여
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    }
            : new String[]{//api 31 미만이면 이러한 권한 부여
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public PermissionManager(AppCompatActivity activity, View view) {
        this.activity = activity;
        this.view = view;
    }
    /**권한 확인*/
    public void checkPermission() {
        for (String pm : permissions) {//foreach로 권한 부여확인
            if (ContextCompat.checkSelfPermission(activity, pm) != PackageManager.PERMISSION_GRANTED) {
                //권한이 없으면 PermissionActivity로 이동해서 권한달라고 요청하도록 함
                Intent intent = new Intent(activity, PermissionActivity.class);
                activity.startActivity(intent);
                activity.finish();
                break;//없으면 액티비티 여러개 열림
            }
        }
    }
    /**권한 부여 확인*/
    public boolean checkPermissions() {
        pref = activity.getPreferences(Context.MODE_PRIVATE);
        boolean isFirst = pref.getBoolean("IsFirst", true);
        Log.i("First", String.valueOf(pref.getBoolean("IsFirst",true)));
        Log.i("First", String.valueOf(isFirst));

        Snackbar snackbar;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    //다시 묻지 않음 || 허용 안함 선택시 -> 설정창 접근
                    snackbar = Snackbar.make(view, "이 앱을 사용하기 위해서는 권한이 필요합니다. \n 권한을 부여하시려면 확인버튼을 눌러주세요", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("확인", v -> {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    });
                    snackbar.show();
                } else {
                    if (isFirst) {
                        //처음 앱 실행 시
                        pref.edit().putBoolean("IsFirst", false).apply();
                        ActivityCompat.requestPermissions(activity, permissions, REQ_CODE);
                    } else {
                        //권한 창에서 뒤로가기 키 누른뒤 다시 권한 부여 눌렀을 때
                        ActivityCompat.requestPermissions(activity, permissions, REQ_CODE);
                    }
                }
                return false;
            }
        }
        return true;
    }
    /**권한 (승인 || 거절) 확인*/
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if ((requestCode == REQ_CODE) && (grantResults.length > 0)) {
            for(int i = 0; i < grantResults.length; i++) {
                //앱 권한 수락시 메인 액티비티로 가기
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                    break;
                } else {
                    Toast toast = Toast.makeText(activity, "앱을 사용하시려면 권한을 허용해야합니다.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }
}