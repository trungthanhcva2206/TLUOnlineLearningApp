package com.nhom1.tlulearningonline;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChiTietKhoaHocActivity extends AppCompatActivity {
    TextView tvTieuDe, tvSoBai, tvTacGia, tvMoTa;
    RecyclerView recyclerView;
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_khoa_hoc);

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
    }
}