package com.cookandroid.movie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cookandroid.movie.helper.PermissionManager;

public class PermissionActivity extends AppCompatActivity {

    private PermissionManager permissionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        View view = findViewById(R.id.PermissionActivity);
        permissionManager = new PermissionManager(this, view);

        Button perbtn = findViewById(R.id.PermissionBtn);
        perbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permissionManager.checkPermissions()) {
                    // 권한이 모두 부여되었을 때의 처리
                    Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
