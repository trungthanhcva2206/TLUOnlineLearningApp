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

public class ChiTietBaiHocActivity extends AppCompatActivity {

    private RecyclerView rvTaiLieu;
    private TaiLieuAdapter taiLieuAdapter;
    private ArrayList<String> danhSachTaiLieu;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_bai_hoc);
        // Trong HomeActivity.java và UserProfileActivity.java
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Ánh xạ view
        TextView tvTenBaiHoc = findViewById(R.id.tvTenBaiHoc);
        ImageView imgVideo = findViewById(R.id.imgVideo);
        ImageView btnBack = findViewById(R.id.btn_back);
        rvTaiLieu = findViewById(R.id.rvTaiLieu);

        // Lấy dữ liệu từ intent
        String tieuDe = getIntent().getStringExtra("tieuDe");
        if (tieuDe != null) {
            tvTenBaiHoc.setText(tieuDe);
        }

        // Sự kiện nút quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity hiện tại
            }
        });

        // Dữ liệu tài liệu mẫu
        danhSachTaiLieu = new ArrayList<>();
        danhSachTaiLieu.add("tai_lieu_1.pdf");
        danhSachTaiLieu.add("tai_lieu_2.pdf");

        // Khởi tạo RecyclerView
        taiLieuAdapter = new TaiLieuAdapter(danhSachTaiLieu, this);
        rvTaiLieu.setLayoutManager(new LinearLayoutManager(this));
        rvTaiLieu.setAdapter(taiLieuAdapter);


        // --- XỬ LÝ THANH ĐIỀU HƯỚNG DƯỚI CÙNG ---
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(ChiTietBaiHocActivity.this, HomeActivity.class));
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    startActivity(new Intent(ChiTietBaiHocActivity.this, GroupChatActivity.class));
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    startActivity(new Intent(ChiTietBaiHocActivity.this, XemKhoaHocActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(ChiTietBaiHocActivity.this, UserProfileActivity.class));
                    // finish();
                    return true;
                }
                return false;
            }
        });
        // Chọn mục "Trang chủ" mặc định khi activity khởi động

    }
}
