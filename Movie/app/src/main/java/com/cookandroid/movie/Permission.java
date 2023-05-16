package com.cookandroid.movie;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Permission {
    private Context context;
    private Activity activity;
    private List<Object> permissionList;
    private final int M_PERMISSIONS = 100;
    private String[] permissions;

            //Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            // Manifest.permission.READ_EXTERNAL_STORAGE,
            // Manifest.permission.WRITE_EXTERNAL_STORAGE,
            // Manifest.permission.READ_MEDIA_AUDIO,
            // Manifest.permission.READ_MEDIA_IMAGES,
            // Manifest.permission.READ_MEDIA_VIDEO


    public Permission(Activity _activity, Context _context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            permissions = new String[]{
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
            };
        }else {
            permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,

            };
        }
        this.activity = _activity;
        this.context = _context;
    }

    public boolean checkPermission(){
        int result;
        permissionList = new ArrayList<>();
        for (String pm : permissions){
            result = ContextCompat.checkSelfPermission(context, pm);
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionList.add(pm);
            }
        }
        return permissionList.isEmpty();
    }

    public void requestPermission(){
        ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]),M_PERMISSIONS);
    }
    public boolean permissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(requestCode == M_PERMISSIONS && (grantResults.length > 0)){
            for(int i = 0; i < grantResults.length; i++){
                if(grantResults[i] == -1){
                    return false;
                }
            }
        }
        return true;
    }
}
