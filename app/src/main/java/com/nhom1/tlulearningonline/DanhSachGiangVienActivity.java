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

        danhSachGiangVien.add(new GiangVien("Nguyễn Văn Nam", "Hệ thống thông tin", R.drawable.gv_ng_van_nam_portrait));
        danhSachGiangVien.add(new GiangVien("Nguyễn Tu Trung", "Hệ thống thông tin", R.drawable.gv_ng_tu_trung_portrait));
        danhSachGiangVien.add(new GiangVien("Nguyễn Thị Thu Hương", "Công nghệ phần mềm", R.drawable.gv_ng_thi_thu_huong));
        danhSachGiangVien.add(new GiangVien("Trương Xuân Nam", "Mạng máy tính", R.drawable.gv_truong_xuan_nam));

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

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(DanhSachGiangVienActivity.this, HomeActivity.class); // Assuming student home is default
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish(); // Remove finish() here unless you explicitly want to remove the current activity from stack
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    Intent intent = new Intent(DanhSachGiangVienActivity.this, GroupChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    Intent intent = SessionManager.getCoursesActivityIntent(DanhSachGiangVienActivity.this); // Use helper
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(DanhSachGiangVienActivity.this, UserProfileActivity.class);
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
