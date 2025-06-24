package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class XemKhoaHocActivity extends AppCompatActivity {

    LinearLayout layoutThamGia, layoutDaLuu;
    ArrayList<KhoaHoc> khoaHocThamGia = new ArrayList<>();
    ArrayList<KhoaHoc> khoaHocDaLuu = new ArrayList<>();

    EditText edtTimKiem;
    TextView tvKhongTimThay;
    private ImageView ivAvatar;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_khoa_hoc);
        // Trong HomeActivity.java và UserProfileActivity.java
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Khởi tạo view sau khi setContentView
        layoutThamGia = findViewById(R.id.layout_khoa_hoc_tham_gia);
        layoutDaLuu = findViewById(R.id.layout_khoa_hoc_da_luu);
        edtTimKiem = findViewById(R.id.edt_tim_kiem);
        tvKhongTimThay = findViewById(R.id.tv_khong_tim_thay);
        ivAvatar = findViewById(R.id.iv_avatar);

        // Dữ liệu mẫu
        khoaHocThamGia.add(new KhoaHoc("Tương tác người máy", "GV: Nguyễn Thị Thu Hương", "Bộ môn Công nghệ phần mềm", 80));
        khoaHocThamGia.add(new KhoaHoc("Công nghệ Web", "GV: Nguyễn Tu Trung", "Bộ môn Hệ thống thông tin", 25));
        khoaHocThamGia.add(new KhoaHoc("Lập trình Java", "GV: Trần Văn B", "Bộ môn Công nghệ phần mềm", 60));
        khoaHocThamGia.add(new KhoaHoc("Cơ sở dữ liệu", "GV: Trần Hồng Diệp", "Bộ môn Hệ thống thông tin", 45));

        khoaHocDaLuu.add(new KhoaHoc("Khai phá dữ liệu", "GV: Lê Thị Tú Kiên", "Bộ môn Hệ thống thông tin", 80));

        // Hiển thị danh sách ban đầu
        hienThiKhoaHoc();

        // Xử lý tìm kiếm
        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                timKiemKhoaHoc(s.toString());
            }
        });

        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(XemKhoaHocActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        // --- XỬ LÝ THANH ĐIỀU HƯỚNG DƯỚI CÙNG ---
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(XemKhoaHocActivity.this, HomeActivity.class));
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    startActivity(new Intent(XemKhoaHocActivity.this, GroupChatActivity.class));
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(XemKhoaHocActivity.this, UserProfileActivity.class));
                    // finish();
                    return true;
                }
                return false;
            }
        });
        // Chọn mục "Trang chủ" mặc định khi activity khởi động
        bottomNavigationView.setSelectedItemId(R.id.nav_courses);
    }

    private void hienThiKhoaHoc() {
        layoutThamGia.removeAllViews();
        layoutDaLuu.removeAllViews();

        for (int i = 0; i < khoaHocThamGia.size(); i++) {
            layoutThamGia.addView(createCard(khoaHocThamGia.get(i), false, i));
        }

        for (int i = 0; i < khoaHocDaLuu.size(); i++) {
            layoutDaLuu.addView(createCard(khoaHocDaLuu.get(i), true, i));
        }
    }

    private void timKiemKhoaHoc(String tuKhoa) {
        layoutThamGia.removeAllViews();

        int dem = 0;
        for (int i = 0; i < khoaHocThamGia.size(); i++) {
            KhoaHoc kh = khoaHocThamGia.get(i);
            if (kh.ten.toLowerCase().contains(tuKhoa.toLowerCase())) {
                layoutThamGia.addView(createCard(kh, false, i));
                dem++;
            }
        }

        if (dem == 0) {
            tvKhongTimThay.setVisibility(View.VISIBLE);
        } else {
            tvKhongTimThay.setVisibility(View.GONE);
        }
    }

    private View createCard(KhoaHoc kh, boolean daLuu, int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_khoa_hoc_sv_blue, null);

        // Đổi màu nền
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
                layoutDaLuu.removeView((View) view.getParent());
                khoaHocDaLuu.remove(kh);
            } else {
                khoaHocDaLuu.add(kh);
                layoutDaLuu.addView(createCard(kh, true, layoutDaLuu.getChildCount()));
            }
        });

        // Mở chi tiết
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

        // ➕ Thêm khoảng cách dưới cho mỗi thẻ
        LinearLayout wrapper = new LinearLayout(this);
        wrapper.setOrientation(LinearLayout.VERTICAL);

        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 345, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        params.setMargins(4, 4, 4, 32);
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
