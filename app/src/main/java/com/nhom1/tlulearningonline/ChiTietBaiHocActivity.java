package com.nhom1.tlulearningonline;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChiTietBaiHocActivity extends AppCompatActivity {

    private RecyclerView rvTaiLieu;
    private TaiLieuAdapter taiLieuAdapter;
    private ArrayList<String> danhSachTaiLieu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_bai_hoc);

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
    }
}
