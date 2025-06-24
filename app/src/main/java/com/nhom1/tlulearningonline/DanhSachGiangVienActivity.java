package com.nhom1.tlulearningonline;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DanhSachGiangVienActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GiangVienAdapter adapter;
    private List<GiangVien> danhSachGiangVien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_giang_vien);

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());


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
    }
}
