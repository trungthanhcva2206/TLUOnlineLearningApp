package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class XemKhoaHocActivity extends AppCompatActivity {

    LinearLayout layoutThamGia, layoutDaLuu;
    ArrayList<KhoaHoc> khoaHocThamGia = new ArrayList<>();
    ArrayList<KhoaHoc> khoaHocDaLuu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_khoa_hoc);

        layoutThamGia = findViewById(R.id.layout_khoa_hoc_tham_gia);
        layoutDaLuu = findViewById(R.id.layout_khoa_hoc_da_luu);

        // Dữ liệu mẫu
        khoaHocThamGia.add(new KhoaHoc("Tương tác người máy", "GV: Nguyễn Thị Thu Hương", "Bộ môn Công nghệ phần mềm", 80));
        khoaHocThamGia.add(new KhoaHoc("Công nghệ Web", "GV: Nguyễn Tu Trung", "Bộ môn Hệ thống thông tin", 25));
        khoaHocThamGia.add(new KhoaHoc("Lập trình Java", "GV: Trần Văn B", "Bộ môn Công nghệ phần mềm", 60));
        khoaHocThamGia.add(new KhoaHoc("Cơ sở dữ liệu", "GV: Trần Hồng Diệp", "Bộ môn Hệ thống thông tin", 45));

        khoaHocDaLuu.add(new KhoaHoc("Khai phá dữ liệu", "GV: Lê Thị Tú Kiên", "Bộ môn Hệ thống thông tin", 80));

        // Hiển thị các khóa học tham gia
        for (int i = 0; i < khoaHocThamGia.size(); i++) {
            View itemView = createCard(khoaHocThamGia.get(i), false, i);
            layoutThamGia.addView(itemView);
        }

        // Hiển thị các khóa học đã lưu
        for (int i = 0; i < khoaHocDaLuu.size(); i++) {
            View itemView = createCard(khoaHocDaLuu.get(i), true, i);
            layoutDaLuu.addView(itemView);
        }
    }

    private View createCard(KhoaHoc kh, boolean daLuu, int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_khoa_hoc_sv, null);

        // Đổi màu nền cho thẻ
        CardView cardView = (CardView) view;
        int colorId = (position % 2 == 0) ? R.color.blue_3 : R.color.blue;
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, colorId));

        // Gán dữ liệu
        CustomViewBinderSV.bind(view, kh);

        // Nút lưu
        ImageView btnSave = view.findViewById(R.id.btn_save);
        btnSave.setImageResource(daLuu ? R.drawable.ic_star_filled : R.drawable.ic_star_border);

        btnSave.setOnClickListener(v -> {
            if (daLuu) {
                layoutDaLuu.removeView((View) view.getParent()); // Xóa wrapper
                khoaHocDaLuu.remove(kh);
            } else {
                khoaHocDaLuu.add(kh);
                layoutDaLuu.addView(createCard(kh, true, layoutDaLuu.getChildCount()));
            }
        });

        // Mở chi tiết khóa học khi click vào card
        view.setOnClickListener(v -> {
            Intent intent = new Intent(XemKhoaHocActivity.this, ChiTietKhoaHocActivity.class);
            intent.putExtra("tieu_de", kh.ten);
            intent.putExtra("mo_ta", "Đây là môn học về " + kh.boMon.toLowerCase() + ", được giảng dạy bởi " + kh.giangVien);
            intent.putExtra("tac_gia", kh.giangVien);
            intent.putExtra("so_bai", 10);

            ArrayList<String> dsBaiHoc = new ArrayList<>();
            for (int j = 1; j <= 10; j++) {
                dsBaiHoc.add("Bài " + j + ": Bài học về " + kh.ten);
            }
            intent.putStringArrayListExtra("ds_bai_hoc", dsBaiHoc);

            startActivity(intent);
        });

        // ➕ Tạo wrapper để thêm khoảng cách dưới cho mỗi thẻ
        LinearLayout wrapper = new LinearLayout(this);
        wrapper.setOrientation(LinearLayout.VERTICAL);

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 345, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(4, 4, 0, 32);
        wrapper.setLayoutParams(params);
        wrapper.addView(view);

        return wrapper;
    }

    public static class KhoaHoc {
        String ten, giangVien, boMon;
        int tienDo;

        public KhoaHoc(String ten, String giangVien, String boMon, int tienDo) {
            this.ten = ten;
            this.giangVien = giangVien;
            this.boMon = boMon;
            this.tienDo = tienDo;
        }
    }
}
