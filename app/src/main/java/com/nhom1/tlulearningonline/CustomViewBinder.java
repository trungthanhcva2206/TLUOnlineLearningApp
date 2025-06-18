package com.nhom1.tlulearningonline;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomViewBinder {
    public static void bind(View view, QuanLyKhoaHocActivity.KhoaHoc kh) {
        TextView tvTieuDe = view.findViewById(R.id.tv_tieu_de);
        TextView tvMoTa = view.findViewById(R.id.tv_mo_ta);
        Button btnSoBai = view.findViewById(R.id.btn_so_bai);
        ImageView btnEdit = view.findViewById(R.id.btn_edit);
        ImageView btnView = view.findViewById(R.id.btn_view);

        tvTieuDe.setText(kh.ten);
        tvMoTa.setText(kh.moTa);
        btnSoBai.setText(kh.getSoBaiHoc() + " bài học");
        // KHÔNG gán setOnClickListener ở đây nữa để tránh ghi đè
    }
}

