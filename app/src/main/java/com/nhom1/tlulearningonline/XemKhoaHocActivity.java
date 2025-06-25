package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button; // Import Button to use btnSoBaiHoc

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import android.widget.ProgressBar; // Import ProgressBar
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class XemKhoaHocActivity extends AppCompatActivity {

    LinearLayout layoutThamGia, layoutDaLuu;
    ArrayList<KhoaHoc> khoaHocThamGia = new ArrayList<>();
    ArrayList<KhoaHoc> khoaHocDaLuu = new ArrayList<>();

    EditText edtTimKiem;
    TextView tvKhongTimThay;
    TextView tv_ten_nguoi_dung;
    ImageView iv_avatar;
    private ImageView ivAvatar;

    private BottomNavigationView bottomNavigationView;
    private Handler sessionHandler;
    private Runnable sessionCheckRunnable;


    private void fetchUserInfo() {
        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getUserId();

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy userId!", Toast.LENGTH_SHORT).show();
            sessionManager.clearSession();
            Intent intent = new Intent(XemKhoaHocActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        String url = "http://14.225.207.221:6060/mobile/users/" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);
        Utf8StringRequest request = new Utf8StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject user = new JSONObject(response);
                        String fullName = user.optString("fullname", "Người dùng");
                        String avatarUrl = user.optString("avatar_url", "");

                        tv_ten_nguoi_dung.setText(fullName + " 👋");

                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            Log.d("AvatarURL", avatarUrl);
                            Glide.with(this)
                                    .load(avatarUrl)
                                    .placeholder(R.drawable.ic_avatar)
                                    .error(R.drawable.ic_avatar)
                                    .circleCrop()
                                    .into(iv_avatar);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý JSON!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                        sessionManager.clearSession();
                        Toast.makeText(this, "Phiên đăng nhập đã hết hạn!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(XemKhoaHocActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        error.printStackTrace();
                        Toast.makeText(this, "Lỗi kết nối tới máy chủ!", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(request);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_khoa_hoc);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        tv_ten_nguoi_dung = findViewById(R.id.tv_ten_nguoi_dung);
        iv_avatar = findViewById(R.id.iv_avatar);

        layoutThamGia = findViewById(R.id.layout_khoa_hoc_tham_gia);
        layoutDaLuu = findViewById(R.id.layout_khoa_hoc_da_luu);
        edtTimKiem = findViewById(R.id.edt_tim_kiem);
        tvKhongTimThay = findViewById(R.id.tv_khong_tim_thay);
        ivAvatar = findViewById(R.id.iv_avatar);

        fetchUserInfo();
        setupSessionCheck();

        khoaHocThamGia.add(new KhoaHoc("Tương tác người máy", "GV: Nguyễn Thị Thu Hương", "Bộ môn Công nghệ phần mềm", 80, 15));
        khoaHocThamGia.add(new KhoaHoc("Công nghệ Web", "GV: Nguyễn Tu Trung", "Bộ môn Hệ thống thông tin", 25, 10));
        khoaHocThamGia.add(new KhoaHoc("Lập trình Java", "GV: Trần Văn B", "Bộ môn Công nghệ phần mềm", 60, 20));
        khoaHocThamGia.add(new KhoaHoc("Cơ sở dữ liệu", "GV: Trần Hồng Diệp", "Bộ môn Hệ thống thông tin", 45, 12));

        khoaHocDaLuu.add(new KhoaHoc("Khai phá dữ liệu", "GV: Lê Thị Tú Kiên", "Bộ môn Hệ thống thông tin", 80, 8));

        hienThiKhoaHoc();

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
        bottomNavigationView.setSelectedItemId(R.id.nav_courses);
    }

    private void hienThiKhoaHoc() {
        layoutThamGia.removeAllViews();
        layoutDaLuu.removeAllViews();

        for (int i = 0; i < khoaHocThamGia.size(); i++) {
            // For "Khóa học đã tham gia", daLuu is false, so it will inflate item_khoa_hoc_sv_progress
            layoutThamGia.addView(createCard(khoaHocThamGia.get(i), false, i));
        }

        for (int i = 0; i < khoaHocDaLuu.size(); i++) {
            // For "Khóa học đã lưu", daLuu is true, so it will inflate item_khoa_hoc_sv
            layoutDaLuu.addView(createCard(khoaHocDaLuu.get(i), true, i));
        }
    }

    private void timKiemKhoaHoc(String tuKhoa) {
        layoutThamGia.removeAllViews();

        int dem = 0;
        for (int i = 0; i < khoaHocThamGia.size(); i++) {
            KhoaHoc kh = khoaHocThamGia.get(i);
            // Search only affects "Khóa học đã tham gia"
            if (kh.ten.toLowerCase().contains(tuKhoa.toLowerCase())) {
                layoutThamGia.addView(createCard(kh, false, i)); // Use item_khoa_hoc_sv_progress
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
        View view;
        TextView txtTenKhoaHoc;
        TextView txtGiangVien;
        TextView txtBoMon;
        ImageView btnSave;
        CardView cardView; // Declare CardView here

        if (!daLuu) { // For "Khóa học đã tham gia" -> use item_khoa_hoc_sv_progress
            view = LayoutInflater.from(this).inflate(R.layout.item_khoa_hoc_sv_progress, null);
            txtTenKhoaHoc = view.findViewById(R.id.txt_ten_khoa_hoc);
            txtGiangVien = view.findViewById(R.id.txt_giang_vien);
            txtBoMon = view.findViewById(R.id.txt_bo_mon);
            btnSave = view.findViewById(R.id.btn_save);

            ProgressBar progressBar = view.findViewById(R.id.progress_bar);
            TextView txtTienDo = view.findViewById(R.id.txt_tien_do);

            txtTenKhoaHoc.setText(kh.ten);
            txtGiangVien.setText(kh.giangVien);
            txtBoMon.setText(kh.boMon);
            progressBar.setProgress(kh.tienDo);
            txtTienDo.setText(kh.tienDo + "%");

        } else { // For "Khóa học đã lưu" -> use item_khoa_hoc_sv
            view = LayoutInflater.from(this).inflate(R.layout.item_khoa_hoc_sv, null);
            txtTenKhoaHoc = view.findViewById(R.id.txt_ten_khoa_hoc);
            txtGiangVien = view.findViewById(R.id.txt_giang_vien);
            txtBoMon = view.findViewById(R.id.txt_bo_mon);
            Button btnSoBaiHoc = view.findViewById(R.id.btn_so_bai_hoc); // Specific to item_khoa_hoc_sv
            btnSave = view.findViewById(R.id.btn_save);

            txtTenKhoaHoc.setText(kh.ten);
            txtGiangVien.setText(kh.giangVien);
            txtBoMon.setText(kh.boMon);
            btnSoBaiHoc.setText(kh.soBaiHoc + " bài học");
        }

        cardView = (CardView) view; // Cast the inflated view to CardView

        // Set alternating background colors
        int colorId = (position % 2 == 0) ? R.color.blue_3 : R.color.blue;
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, colorId));

        // Set star icon (common to both)
        btnSave.setImageResource(daLuu ? R.drawable.ic_star_filled : R.drawable.ic_star_border);

        btnSave.setOnClickListener(v -> {
            if (daLuu) {
                layoutDaLuu.removeView((View) v.getParent().getParent().getParent()); // Correctly remove the wrapper view
                khoaHocDaLuu.remove(kh);
            } else {
                khoaHocDaLuu.add(kh);
                layoutDaLuu.addView(createCard(kh, true, layoutDaLuu.getChildCount()));
            }
        });

        view.setOnClickListener(v -> {
            Intent intent = new Intent(XemKhoaHocActivity.this, ChiTietKhoaHocActivity.class);
            intent.putExtra("tieu_de", kh.ten);
            intent.putExtra("mo_ta", "Đây là môn học về " + kh.boMon.toLowerCase() + ", được giảng dạy bởi " + kh.giangVien);
            intent.putExtra("tac_gia", kh.giangVien);
            intent.putExtra("so_bai", kh.soBaiHoc);

            ArrayList<String> dsBaiHoc = new ArrayList<>();
            for (int j = 1; j <= kh.soBaiHoc; j++) {
                dsBaiHoc.add("Bài " + j + ": Bài học về " + kh.ten);
            }
            intent.putStringArrayListExtra("ds_bai_hoc", dsBaiHoc);

            startActivity(intent);
        });

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

    private void setupSessionCheck() {
        sessionHandler = new Handler(Looper.getMainLooper());
        sessionCheckRunnable = () -> {
            SessionManager sessionManager = new SessionManager(XemKhoaHocActivity.this);
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(XemKhoaHocActivity.this, "Phiên đăng nhập đã hết hạn!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(XemKhoaHocActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                sessionHandler.postDelayed(sessionCheckRunnable, 10000); // 10 giây kiểm tra lại
            }
        };
        sessionHandler.postDelayed(sessionCheckRunnable, 10000); // bắt đầu sau 10 giây
    }
    public static class KhoaHoc {
        String ten, giangVien, boMon;
        int tienDo;
        int soBaiHoc;

        public KhoaHoc(String ten, String giangVien, String boMon, int tienDo, int soBaiHoc) {
            this.ten = ten;
            this.giangVien = giangVien;
            this.boMon = boMon;
            this.tienDo = tienDo;
            this.soBaiHoc = soBaiHoc;
        }

        public KhoaHoc(String ten, String giangVien, String boMon, int tienDo) {
            this(ten, giangVien, boMon, tienDo, 0); // Mặc định số bài học là 0
        }
    }
}