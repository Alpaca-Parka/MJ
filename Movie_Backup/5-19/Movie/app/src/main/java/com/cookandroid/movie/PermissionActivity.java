package com.cookandroid.movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;


public class PermissionActivity extends AppCompatActivity {

    String[] permissions;
    //private List<Object> permissionList;
    private static final int REQ_CODE = 1;
    //private View view = findViewById(R.id.PermissionActivity);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Button perbtn = (Button) findViewById(R.id.PermissionBtn);
        perbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPermissionGranted()){
                    Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_AUDIO,
                    Manifest.permission.READ_MEDIA_VIDEO
            };
        } else {
            permissions = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }


        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        boolean isFirst = pref.getBoolean("IsFirst", true);
        View view = findViewById(R.id.PermissionActivity);
        Snackbar snackbar = Snackbar.make(view, "text1", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("확인", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        for (String pm : permissions) {
            if (ContextCompat.checkSelfPermission(this, pm) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, pm)) {
                    Log.v("testbtn","1");
                    snackbar = Snackbar.make(view, "이 앱을 사용하기 위해서는 권한이 필요합니다. \n 권한을 부여하시려면 확인버튼을 눌러주세요", Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("확인", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    });
                    snackbar.show();
                    // 거부만 한 경우 사용자에게 왜 필요한지 이유를 설명해주는게 좋다
                } else {
                    if (isFirst) {
                        // 처음 물었는지 여부를 저장
                        Log.v("testbtn","2");
                        pref.edit().putBoolean("IsFirst", false).apply();
                        // 권한요청
                        ActivityCompat.requestPermissions(this, permissions, REQ_CODE);
                    } else {
                        // 사용자가 권한을 거부하면서 다시 묻지않음 옵션을 선택한 경우
                        // requestPermission을 요청해도 창이 나타나지 않기 때문에 설정창으로 이동한다.
                        Log.v("testbtn","3");
                        snackbar = Snackbar.make(view, "이 앱을 사용하기 위해서는 권한이 필요합니다. \n 권한을 부여하시려면 확인버튼을 눌러주세요", Snackbar.LENGTH_INDEFINITE);
                        snackbar.setAction("확인", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(PermissionActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.v("test3","onrequestper");
            }
        }
    }
}
