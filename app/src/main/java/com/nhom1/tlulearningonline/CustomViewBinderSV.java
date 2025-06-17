package com.nhom1.tlulearningonline;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CustomViewBinderSV {

    public static void bind(View view, XemKhoaHocActivity.KhoaHoc khoaHoc) {
        TextView txtTenKhoaHoc = view.findViewById(R.id.txt_ten_khoa_hoc);
        TextView txtGiangVien = view.findViewById(R.id.txt_giang_vien);
        TextView txtBoMon = view.findViewById(R.id.txt_bo_mon);
        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        TextView txtTienDo = view.findViewById(R.id.txt_tien_do);

        txtTenKhoaHoc.setText(khoaHoc.ten);
        txtGiangVien.setText(khoaHoc.giangVien);
        txtBoMon.setText(khoaHoc.boMon);
        progressBar.setProgress(khoaHoc.tienDo);
        txtTienDo.setText(khoaHoc.tienDo + "%");
    }
}
