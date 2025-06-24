package com.nhom1.tlulearningonline;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class DanhSachGiangVienActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GiangVienAdapter adapter;
    private List<GiangVien> danhSachGiangVien;

    private ImageView ivAvatar;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_giang_vien);

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ivAvatar = findViewById(R.id.iv_avatar);


        recyclerView = findViewById(R.id.recyclerGiangVien);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        danhSachGiangVien = new ArrayList<>();
        int avatarMacDinh = R.drawable.ic_avatar;

        danhSachGiangVien.add(new GiangVien("Nguyễn Văn Nam", "Hệ thống thông tin", avatarMacDinh));
        danhSachGiangVien.add(new GiangVien("Trương Xuân Nam", "Mạng máy tính", avatarMacDinh));
        danhSachGiangVien.add(new GiangVien("Nguyễn Thị Thu Hương", "Công nghệ phần mềm", avatarMacDinh));
        // Thêm giảng viên khác

        adapter = new GiangVienAdapter(danhSachGiangVien, giangVien -> {
            Intent intent = new Intent(this, ChiTietGiangVienActivity.class);
            intent.putExtra("ten", giangVien.getTen());
            intent.putExtra("boMon", giangVien.getBoMon());
            intent.putExtra("avatarResId", giangVien.getAvatarResId());
            startActivity(intent);
        });


        recyclerView.setAdapter(adapter);

        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSachGiangVienActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        // --- XỬ LÝ THANH ĐIỀU HƯỚNG DƯỚI CÙNG ---
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(DanhSachGiangVienActivity.this, HomeActivity.class));
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    startActivity(new Intent(DanhSachGiangVienActivity.this, GroupChatActivity.class));
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    startActivity(new Intent(DanhSachGiangVienActivity.this, XemKhoaHocActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(DanhSachGiangVienActivity.this, UserProfileActivity.class));
                    // finish();
                    return true;
                }
                return false;
            }
        });
        // Chọn mục "Trang chủ" mặc định khi activity khởi động

    }
}
