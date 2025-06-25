package com.nhom1.tlulearningonline;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ChiTietKhoaHocActivity extends AppCompatActivity {
    TextView tvTieuDe, tvSoBai, tvTacGia, tvMoTa;
    RecyclerView recyclerView;
    ImageView btnBack;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_khoa_hoc);

        // Trong HomeActivity.java và UserProfileActivity.java
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Ánh xạ
        tvTieuDe = findViewById(R.id.tv_course_name);
        tvSoBai = findViewById(R.id.tv_lesson_count);
        tvTacGia = findViewById(R.id.tv_teacher);
        tvMoTa = findViewById(R.id.tv_description);
        recyclerView = findViewById(R.id.recycler_lessons);
        btnBack = findViewById(R.id.btn_back);

        // Nhận dữ liệu từ intent
        Intent intent = getIntent();
        String tieuDe = intent.getStringExtra("tieu_de");
        String moTa = intent.getStringExtra("mo_ta");
        String tacGia = intent.getStringExtra("tac_gia");
        int soBai = intent.getIntExtra("so_bai", 0);

        // Hiển thị thông tin khóa học
        tvTieuDe.setText(tieuDe);
        tvMoTa.setText(moTa);
        tvTacGia.setText("👩‍🏫 " + tacGia);
        tvSoBai.setText("📘 " + soBai + " bài học");

        // Tạo danh sách bài học với thời lượng
        ArrayList<Lesson> danhSachBaiHoc = new ArrayList<>();
        String[] thoiLuongMau = {"25 phút", "30 phút", "20 phút", "35 phút", "28 phút",
                "22 phút", "40 phút", "18 phút", "33 phút", "27 phút"};

        for (int i = 1; i <= soBai; i++) {
            String thoiLuong = i <= thoiLuongMau.length ? thoiLuongMau[i-1] : "25 phút";
            danhSachBaiHoc.add(new Lesson("Bài " + i + " - Giới thiệu khái niệm", thoiLuong));
        }

        // Cài đặt RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BaiHocAdapter adapter = new BaiHocAdapter(danhSachBaiHoc,ChiTietKhoaHocActivity.this);
        recyclerView.setAdapter(adapter);

        // Xử lý nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity hiện tại và quay lại
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(ChiTietKhoaHocActivity.this, HomeActivity.class); // Assuming student home is default
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish(); // Remove finish() here unless you explicitly want to remove the current activity from stack
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    Intent intent = new Intent(ChiTietKhoaHocActivity.this, GroupChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    Intent intent = SessionManager.getCoursesActivityIntent(ChiTietKhoaHocActivity.this); // Use helper
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(ChiTietKhoaHocActivity.this, UserProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                }
                return false;
            }
        });

    }
}