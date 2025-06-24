package com.nhom1.tlulearningonline;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhom1.tlulearningonline.KhoaHocAdapter;
import com.nhom1.tlulearningonline.KhoaHoc;

public class ChiTietGiangVienActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvTenGv, tvBoMon, tvEmail;
    private RecyclerView recyclerKhoaHoc;
    private KhoaHocAdapter adapter;
    private List<KhoaHoc> khoaHocList;
    private ImageView ivAvatar;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_giang_vien);

        // Trong HomeActivity.java và UserProfileActivity.java
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        btnBack = findViewById(R.id.btn_back);
        tvTenGv = findViewById(R.id.tv_ten_gv);
        tvBoMon = findViewById(R.id.tv_bo_mon);
        tvEmail = findViewById(R.id.tv_email);
        recyclerKhoaHoc = findViewById(R.id.recycler_khoa_hoc);
        ivAvatar = findViewById(R.id.iv_avatar);

        Intent intent = getIntent();
        String ten = intent.getStringExtra("ten");
        String boMon = intent.getStringExtra("boMon");
        int avatarResId = intent.getIntExtra("avatarResId", R.drawable.ic_avatar);

        tvTenGv.setText(ten);
        tvBoMon.setText(boMon);
        tvEmail.setText(ten.toLowerCase().replace(" ", "") + "@tlu.edu.vn");
        ((ImageView) findViewById(R.id.img_giang_vien)).setImageResource(avatarResId);


        // Back
        btnBack.setOnClickListener(v -> finish());

        // Danh sách khóa học (demo)
        khoaHocList = new ArrayList<>();
        khoaHocList.add(new KhoaHoc("Phát triển ứng dụng trên thiết bị di động", "Nguyễn Văn Nam", "Hệ thống thông tin", 10));
        khoaHocList.add(new KhoaHoc("Phân tích và thiết kế HTTT", "Nguyễn Văn Nam", "Hệ thống thông tin", 15));
        khoaHocList.add(new KhoaHoc("Phát triển ứng dụng trên thiết bị di động", "Nguyễn Văn Nam", "Hệ thống thông tin", 5));

        adapter = new KhoaHocAdapter(khoaHocList);
        recyclerKhoaHoc.setLayoutManager(new LinearLayoutManager(this));
        recyclerKhoaHoc.setAdapter(adapter);

        ivAvatar.setOnClickListener(v -> {
            Intent intent1 = new Intent(ChiTietGiangVienActivity.this, UserProfileActivity.class);
            startActivity(intent1);
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(ChiTietGiangVienActivity.this, HomeActivity.class); // Assuming student home is default
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish(); // Remove finish() here unless you explicitly want to remove the current activity from stack
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    Intent intent = new Intent(ChiTietGiangVienActivity.this, GroupChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    Intent intent = SessionManager.getCoursesActivityIntent(ChiTietGiangVienActivity.this); // Use helper
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(ChiTietGiangVienActivity.this, UserProfileActivity.class);
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
