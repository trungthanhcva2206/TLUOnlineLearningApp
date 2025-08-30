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
import java.util.HashMap;

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

        fetchKhoaHocThamGia();


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
            layoutThamGia.addView(createCard(khoaHocThamGia.get(i), true, i));
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
            intent.putExtra("course_id", kh.id);
            intent.putExtra("tieu_de", kh.ten);
            intent.putExtra("des", kh.des);
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
        String id, ten, giangVien, boMon, des;
        int tienDo;
        int soBaiHoc;

        public KhoaHoc(String id, String ten, String giangVien, String boMon, int tienDo, int soBaiHoc, String des) {
            this.id = id;
            this.ten = ten;
            this.giangVien = giangVien;
            this.boMon = boMon;
            this.tienDo = tienDo;
            this.soBaiHoc = soBaiHoc;
            this.des = des;
        }

        public String getDes() {
            return des;
        }

        public String getId() { return id; }
        public String getTen() { return ten; }
        public String getGiangVien() { return giangVien; }
        public String getBoMon() { return boMon; }
        public int getSoBaiHoc() { return soBaiHoc; }
    }

    private void fetchKhoaHocThamGia() {
        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getUserId();
        if (userId == null || userId.isEmpty()) return;

        String regUrl = "http://14.225.207.221:6060/mobile/course-registrations";
        String courseUrl = "http://14.225.207.221:6060/mobile/courses";
        String lessonUrl = "http://14.225.207.221:6060/mobile/lessons";

        RequestQueue queue = Volley.newRequestQueue(this);

        // Step 1: Lấy danh sách bài học
        Utf8StringRequest lessonRequest = new Utf8StringRequest(Request.Method.GET, lessonUrl,
                lessonResponse -> {
                    try {
                        org.json.JSONArray lessonArray = new org.json.JSONArray(lessonResponse);
                        HashMap<String, Integer> lessonCountMap = new HashMap<>();

                        for (int i = 0; i < lessonArray.length(); i++) {
                            JSONObject lesson = lessonArray.getJSONObject(i);
                            String courseId = lesson.getString("courseId");
                            int currentCount = lessonCountMap.getOrDefault(courseId, 0);
                            lessonCountMap.put(courseId, currentCount + 1);
                        }

                        // Step 2: Lấy danh sách đăng ký
                        Utf8StringRequest regRequest = new Utf8StringRequest(Request.Method.GET, regUrl,
                                regResponse -> {
                                    try {
                                        ArrayList<String> courseIds = new ArrayList<>();
                                        org.json.JSONArray regArray = new org.json.JSONArray(regResponse);

                                        for (int i = 0; i < regArray.length(); i++) {
                                            JSONObject reg = regArray.getJSONObject(i);
                                            if (userId.equals(reg.getString("userId"))) {
                                                courseIds.add(reg.getString("courseId"));
                                            }
                                        }

                                        // Step 3: Lấy thông tin khóa học
                                        Utf8StringRequest courseRequest = new Utf8StringRequest(Request.Method.GET, courseUrl,
                                                courseResponse -> {
                                                    try {
                                                        org.json.JSONArray courseArray = new org.json.JSONArray(courseResponse);
                                                        khoaHocThamGia.clear();

                                                        for (int i = 0; i < courseArray.length(); i++) {
                                                            JSONObject course = courseArray.getJSONObject(i);
                                                            String id = course.getString("id");

                                                            if (courseIds.contains(id)) {
                                                                String title = course.getString("title");
                                                                String teacher = course.optString("teacherName", "Chưa rõ");
                                                                String department = course.optString("departmentName", "Chưa rõ");
                                                                String des = course.optString("description", "Chưa rõ");
                                                                int lessonCount = lessonCountMap.getOrDefault(id, 0);


                                                                KhoaHoc kh = new KhoaHoc(id, title, teacher, department, 0, lessonCount,des);
                                                                khoaHocThamGia.add(kh);
                                                            }
                                                        }

                                                        hienThiKhoaHoc(); // Gọi để hiển thị

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                        Toast.makeText(this, "Lỗi xử lý courses!", Toast.LENGTH_SHORT).show();
                                                    }
                                                },
                                                error -> {
                                                    Toast.makeText(this, "Lỗi khi gọi /courses", Toast.LENGTH_SHORT).show();
                                                    error.printStackTrace();
                                                });

                                        queue.add(courseRequest);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(this, "Lỗi xử lý registrations!", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                error -> {
                                    Toast.makeText(this, "Lỗi khi gọi /course-registrations", Toast.LENGTH_SHORT).show();
                                    error.printStackTrace();
                                });

                        queue.add(regRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý lessons!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(this, "Lỗi khi gọi /lessons", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                });

        queue.add(lessonRequest);
    }



}