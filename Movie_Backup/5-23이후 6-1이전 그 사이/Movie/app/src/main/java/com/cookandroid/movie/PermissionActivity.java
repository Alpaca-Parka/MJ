package com.cookandroid.movie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cookandroid.movie.helpers.DoubleBackPressHandler;
import com.cookandroid.movie.helpers.PermissionManager;

import org.jetbrains.annotations.NotNull;

public class PermissionActivity extends AppCompatActivity {

    private PermissionManager permissionManager;
    private final DoubleBackPressHandler doubleBackPressHandler = new DoubleBackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        View view = findViewById(R.id.PermissionActivity);
        permissionManager = new PermissionManager(this, view);

        Button perbtn = findViewById(R.id.PermissionBtn);
        perbtn.setOnClickListener(v -> {
            if (permissionManager.checkPermissions()) {
                // 권한이 모두 부여되었을 때의 처리
                Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    public void onBackPressed() {
        doubleBackPressHandler.onBackPressed();
    }
}
