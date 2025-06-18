package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tự động chuyển sang XemKhoaHocActivity
        Intent intent = new Intent(MainActivity.this, QuanLyKhoaHocActivity.class);
        startActivity(intent);

        // Kết thúc MainActivity để không quay lại được khi bấm nút back
        finish();
    }
}