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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cookandroid.movie.MainActivity;
import com.cookandroid.movie.PermissionActivity;
import com.google.android.material.snackbar.Snackbar;

public class PermissionManager {
    private static final int REQ_CODE = 1;
    private AppCompatActivity activity;
    private View view;
    private String[] permissions = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            ? new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    }
            : new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public PermissionManager(AppCompatActivity activity, View view) {
        this.activity = activity;
        this.view = view;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            permissions = new String[]{
//                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.READ_MEDIA_AUDIO,
//                    Manifest.permission.READ_MEDIA_VIDEO
//            };
//        } else {
//            permissions = new String[]{
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//            };
//        }
    }
    /**권한 확인*/
    public void checkPermission() {
        for (String pm : permissions) {
            if (ContextCompat.checkSelfPermission(activity, pm) != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(activity, PermissionActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }
    /**권한 부여 확인*/
    public boolean checkPermissions() {
        SharedPreferences pref = activity.getPreferences(Context.MODE_PRIVATE);
        boolean isFirst = pref.getBoolean("IsFirst", true);
        Snackbar snackbar;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    snackbar = Snackbar.make(view, "이 앱을 사용하기 위해서는 권한이 필요합니다. \n 권한을 부여하시려면 확인버튼을 눌러주세요", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                            intent.setData(uri);
                            activity.startActivity(intent);
                        }
                    });
                    snackbar.show();
                } else {
                    if (isFirst) {
                        pref.edit().putBoolean("IsFirst", false).apply();
                        ActivityCompat.requestPermissions(activity, permissions, REQ_CODE);
                    } else {
                        snackbar = Snackbar.make(view, "이 앱을 사용하기 위해서는 권한이 필요합니다. \n 권한을 부여하시려면 확인버튼을 눌러주세요", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("확인", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                                intent.setData(uri);
                                activity.startActivity(intent);
                            }
                        });
                        snackbar.show();
                    }
                }
                return false;
            }
        }
        return true;
    }
    /**권한 (승인 || 거절) 확인*/
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            } else {
                // 권한이 거부되었을 때의 처리
            }
        }
    }
}
