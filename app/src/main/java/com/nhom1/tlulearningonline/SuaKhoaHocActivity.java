package com.nhom1.tlulearningonline;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class SuaKhoaHocActivity extends AppCompatActivity {

    private TextInputLayout layoutTenKhoaHoc, layoutMoTaKhoaHoc;
    private TextInputEditText edtTenKhoaHoc, edtMoTaKhoaHoc;
    private Button btnThemBaiHoc, btnCapNhatKhoaHoc;
    private LinearLayout layoutDanhSachBaiHoc;

    private final List<BaiHoc> danhSachBaiHoc = new ArrayList<>();
    private static final int REQUEST_THEM_BAI_HOC = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_khoa_hoc);

        edtTenKhoaHoc = findViewById(R.id.edt_ten_khoa_hoc);
        edtMoTaKhoaHoc = findViewById(R.id.edt_mo_ta_khoa_hoc);
        layoutTenKhoaHoc = findViewById(R.id.layout_ten_khoa_hoc);
        layoutMoTaKhoaHoc = findViewById(R.id.layout_mo_ta_khoa_hoc);
        btnThemBaiHoc = findViewById(R.id.btn_them_bai_hoc);
        btnCapNhatKhoaHoc = findViewById(R.id.btn_sua_khoa_hoc);
        layoutDanhSachBaiHoc = findViewById(R.id.layout_danh_sach_bai_hoc);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String ten = intent.getStringExtra("ten");
        String moTa = intent.getStringExtra("mo_ta");
        ArrayList<String> dsBaiHoc = intent.getStringArrayListExtra("ds_bai_hoc");

        if (ten != null) edtTenKhoaHoc.setText(ten);
        if (moTa != null) edtMoTaKhoaHoc.setText(moTa);

        // Hiển thị danh sách bài học (nếu có)
        if (dsBaiHoc != null) {
            for (String tieuDe : dsBaiHoc) {
                BaiHoc bh = new BaiHoc(tieuDe, "", "");
                danhSachBaiHoc.add(bh);
                addBaiHocToLayout(bh);
            }
        }

        btnThemBaiHoc.setOnClickListener(v -> {
            Intent i = new Intent(SuaKhoaHocActivity.this, ThemBaiHocActivity.class);
            startActivityForResult(i, REQUEST_THEM_BAI_HOC);
        });

        btnCapNhatKhoaHoc.setOnClickListener(v -> {
            String tenKhoaHoc = edtTenKhoaHoc.getText().toString().trim();
            String moTaKhoaHoc = edtMoTaKhoaHoc.getText().toString().trim();

            layoutTenKhoaHoc.setError(null);
            layoutMoTaKhoaHoc.setError(null);

            boolean isValid = true;

            if (TextUtils.isEmpty(tenKhoaHoc)) {
                layoutTenKhoaHoc.setError("Vui lòng nhập tên khóa học");
                isValid = false;
            }

            if (TextUtils.isEmpty(moTaKhoaHoc)) {
                layoutMoTaKhoaHoc.setError("Vui lòng nhập mô tả khóa học");
                isValid = false;
            }

            if (danhSachBaiHoc.isEmpty()) {
                Toast.makeText(this, "Vui lòng thêm ít nhất 1 bài học", Toast.LENGTH_SHORT).show();
                isValid = false;
            }

            if (isValid) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("tenKhoaHoc", tenKhoaHoc);
                resultIntent.putExtra("moTaKhoaHoc", moTaKhoaHoc);

                ArrayList<String> dsTenBaiHoc = new ArrayList<>();
                for (BaiHoc bh : danhSachBaiHoc) {
                    dsTenBaiHoc.add(bh.ten);
                }

                resultIntent.putStringArrayListExtra("ds_bai_hoc", dsTenBaiHoc);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_THEM_BAI_HOC && resultCode == RESULT_OK && data != null) {
            String tenBaiHoc = data.getStringExtra("tenBaiHoc");
            String videoUri = data.getStringExtra("videoUri");
            String taiLieu = data.getStringExtra("tenTaiLieu");

            BaiHoc baiHoc = new BaiHoc(tenBaiHoc, videoUri, taiLieu);
            danhSachBaiHoc.add(baiHoc);
            addBaiHocToLayout(baiHoc);
        }
    }

    private void addBaiHocToLayout(BaiHoc baiHoc) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_bai_hoc_gv, layoutDanhSachBaiHoc, false);
        TextView tvTenBaiHoc = view.findViewById(R.id.tvTenBaiHoc);
        ImageView btnXoa = view.findViewById(R.id.btnXoaBaiHoc);

        tvTenBaiHoc.setText("- " + baiHoc.ten);
        btnXoa.setOnClickListener(v -> {
            layoutDanhSachBaiHoc.removeView(view);
            danhSachBaiHoc.remove(baiHoc);
        });

        layoutDanhSachBaiHoc.addView(view);
    }

    private static class BaiHoc {
        String ten;
        String videoUri;
        String taiLieu;

        public BaiHoc(String ten, String videoUri, String taiLieu) {
            this.ten = ten;
            this.videoUri = videoUri;
            this.taiLieu = taiLieu;
        }
    }
}
