package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class QuanLyKhoaHocActivity extends AppCompatActivity {

    private static final int REQUEST_TAO_KHOA_HOC = 100;
    private static final int REQUEST_SUA_KHOA_HOC = 200;

    private LinearLayout layoutDsKhoaHoc;
    private final ArrayList<KhoaHoc> danhSach = new ArrayList<>();
    private int viTriDangSua = -1;

    private BottomNavigationView bottomNavigationView; // Declared here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_khoa_hoc);

        // Initialize bottomNavigationView here
        bottomNavigationView = findViewById(R.id.bottom_navigation); // Add this line

        layoutDsKhoaHoc = findViewById(R.id.layout_ds_khoa_hoc);
        Button btnTaoKhoaHoc = findViewById(R.id.btn_tao_khoa_hoc);

        // Dữ liệu mẫu
        ArrayList<String> baiHocMau = new ArrayList<>();
        baiHocMau.add("Bài 1: Giới thiệu");
        baiHocMau.add("Bài 2: Thiết kế giao diện");
        baiHocMau.add("Bài 3: Nguyên tắc HCI");

        danhSach.add(new KhoaHoc("Tương tác người máy", "Môn học về nguyên tắc thiết kế giao diện", new ArrayList<>(baiHocMau)));
        danhSach.add(new KhoaHoc("Trí tuệ nhân tạo", "Giới thiệu về AI và ứng dụng", new ArrayList<>(baiHocMau)));

        for (int i = 0; i < danhSach.size(); i++) {
            addKhoaHocToLayout(danhSach.get(i), i);
        }

        btnTaoKhoaHoc.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLyKhoaHocActivity.this, TaoKhoaHocActivity.class);
            startActivityForResult(intent, REQUEST_TAO_KHOA_HOC);
        });

        // --- XỬ LÝ THANH ĐIỀU HƯỚNG DƯỚI CÙNG ---
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    startActivity(new Intent(QuanLyKhoaHocActivity.this, HomeGVActivity.class));
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    startActivity(new Intent(QuanLyKhoaHocActivity.this, GroupChatActivity.class));
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(QuanLyKhoaHocActivity.this, UserProfileActivity.class));
                    // finish();
                    return true;
                }
                return false;
            }
        });
        // Chọn mục "Trang chủ" mặc định khi activity khởi động
        bottomNavigationView.setSelectedItemId(R.id.nav_courses);
    }

    private void addKhoaHocToLayout(KhoaHoc kh, int position) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.item_khoa_hoc, layoutDsKhoaHoc, false);
        CardView cardView = (CardView) itemView;

        int colorId = (position % 2 == 0) ? R.color.blue_3 : R.color.blue;
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, colorId));

        CustomViewBinder.bind(itemView, kh);

        ImageView btnView = itemView.findViewById(R.id.btn_view);
        ImageView btnEdit = itemView.findViewById(R.id.btn_edit);
        btnView.setTag(false);

        btnView.setOnClickListener(v -> showConfirmationDialog(itemView, btnView, btnEdit));

        btnEdit.setOnClickListener(v -> {
            viTriDangSua = position;
            Intent intent = new Intent(QuanLyKhoaHocActivity.this, SuaKhoaHocActivity.class);
            intent.putExtra("ten", kh.ten);
            intent.putExtra("mo_ta", kh.moTa);
            intent.putStringArrayListExtra("ds_bai_hoc", kh.dsBaiHoc);
            startActivityForResult(intent, REQUEST_SUA_KHOA_HOC);
        });

        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLyKhoaHocActivity.this, ChiTietKhoaHocActivity.class);
            intent.putExtra("tieu_de", kh.ten);
            intent.putExtra("mo_ta", kh.moTa);
            intent.putExtra("tac_gia", "GV. Nguyễn Văn A");
            intent.putExtra("so_bai", kh.dsBaiHoc.size());
            intent.putStringArrayListExtra("ds_bai_hoc", kh.dsBaiHoc);
            startActivity(intent);
        });

        layoutDsKhoaHoc.addView(itemView);
    }

    // --- PHIÊN BẢN ĐÃ SỬA LỖI VÀ TỐI ƯU ---
    private void showConfirmationDialog(final View itemView, final ImageView btnView, final ImageView btnEdit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_hide, null);
        builder.setView(dialogView);

        final AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        TextView tvMessage = dialogView.findViewById(R.id.tv_dialog_message);
        Button btnConfirm = dialogView.findViewById(R.id.btn_dialog_confirm);
        Button btnCancel = dialogView.findViewById(R.id.btn_dialog_cancel);

        boolean isCurrentlyHidden = (boolean) btnView.getTag();
        String actionText = isCurrentlyHidden ? "hiện" : "ẩn";
        tvMessage.setText("Bạn chắc chắn muốn " + actionText + " khóa học này?");

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            boolean isHidden = (boolean) btnView.getTag();
            boolean willBeVisible = isHidden; // Nếu đang ẩn (true), nó sẽ trở nên visible (true)

            // Cập nhật giao diện
            itemView.setAlpha(willBeVisible ? 1.0f : 0.3f);
            btnView.setImageResource(willBeVisible ? R.drawable.ic_visibility : R.drawable.ic_visibility_off);

            // === PHẦN SỬA LỖI QUAN TRỌNG ===
            // Kích hoạt/Vô hiệu hóa khả năng tương tác
            itemView.setClickable(willBeVisible);
            itemView.setFocusable(willBeVisible);
            btnEdit.setEnabled(willBeVisible); // Vô hiệu hóa/Kích hoạt cả nút sửa
            btnEdit.setAlpha(willBeVisible ? 1.0f : 0.5f); // Làm mờ nút sửa khi ẩn

            // Cập nhật lại trạng thái
            btnView.setTag(!isHidden);

            dialog.dismiss();
        });

        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String ten = data.getStringExtra("tenKhoaHoc");
            String moTa = data.getStringExtra("moTaKhoaHoc");
            ArrayList<String> dsBaiHoc = data.getStringArrayListExtra("ds_bai_hoc");
            if (dsBaiHoc == null) dsBaiHoc = new ArrayList<>();
            if (requestCode == REQUEST_TAO_KHOA_HOC) {
                KhoaHoc khoaHocMoi = new KhoaHoc(ten, moTa, dsBaiHoc);
                danhSach.add(khoaHocMoi);
                addKhoaHocToLayout(khoaHocMoi, danhSach.size() - 1);
            } else if (requestCode == REQUEST_SUA_KHOA_HOC && viTriDangSua != -1) {
                KhoaHoc khoaHoc = danhSach.get(viTriDangSua);
                khoaHoc.ten = ten;
                khoaHoc.moTa = moTa;
                khoaHoc.dsBaiHoc = dsBaiHoc;
                layoutDsKhoaHoc.removeAllViews();
                for (int i = 0; i < danhSach.size(); i++) {
                    addKhoaHocToLayout(danhSach.get(i), i);
                }
                viTriDangSua = -1;
            }
        }
    }
    public static class KhoaHoc {
        String ten, moTa;
        ArrayList<String> dsBaiHoc;
        public KhoaHoc(String ten, String moTa, ArrayList<String> dsBaiHoc) {
            this.ten = ten;
            this.moTa = moTa;
            this.dsBaiHoc = dsBaiHoc != null ? dsBaiHoc : new ArrayList<>();
        }
        public int getSoBaiHoc() { return dsBaiHoc.size(); }
    }
    static class CustomViewBinder {
        public static void bind(View itemView, KhoaHoc kh) {
            TextView tvTieuDe = itemView.findViewById(R.id.tv_tieu_de);
            TextView tvMoTa = itemView.findViewById(R.id.tv_mo_ta);
            Button btnSoBai = itemView.findViewById(R.id.btn_so_bai);
            tvTieuDe.setText(kh.ten);
            tvMoTa.setText(kh.moTa);
            btnSoBai.setText(kh.getSoBaiHoc() + " bài học");
        }
    }
}