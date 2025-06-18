package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnChat, btnXemKhoaHoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main); // Sử dụng layout chứa các nút

        btnChat = findViewById(R.id.btnChat);

        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GroupChatActivity.class);
            startActivity(intent);
        });

        btnXemKhoaHoc = findViewById(R.id.btnXemKhoaHoc);

        btnXemKhoaHoc.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, XemKhoaHocActivity.class);
            startActivity(intent);
        });

    }
}