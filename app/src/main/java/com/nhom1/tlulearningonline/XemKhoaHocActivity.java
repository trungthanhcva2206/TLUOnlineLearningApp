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
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import android.widget.ProgressBar;
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

    // Thêm tham chiếu đến LinearLayout chứa các filter chips
    private LinearLayout layoutFilterChips;


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

        // Ánh xạ LinearLayout chứa các filter chips
        LinearLayout horizontalChipContainer = findViewById(R.id.horizontal_chip_container); // ID của LinearLayout bên trong HorizontalScrollView
        if (horizontalChipContainer != null) {
            layoutFilterChips = horizontalChipContainer;
        }

        fetchUserInfo();
        setupSessionCheck();

<<<<<<< feature/45-fix-UI
        // Dữ liệu mẫu (nếu bạn có dữ liệu thật từ API thì bỏ qua phần này)
        khoaHocThamGia.add(new KhoaHoc("Tương tác người máy", "GV: Nguyễn Thị Thu Hương", "Bộ môn Công nghệ phần mềm", 80, 15));
        khoaHocThamGia.add(new KhoaHoc("Công nghệ Web", "GV: Nguyễn Tu Trung", "Bộ môn Hệ thống thông tin", 25, 10));
        khoaHocThamGia.add(new KhoaHoc("Lập trình Java", "GV: Trần Văn B", "Bộ môn Công nghệ phần mềm", 60, 20));
        khoaHocThamGia.add(new KhoaHoc("Cơ sở dữ liệu", "GV: Trần Hồng Diệp", "Bộ môn Hệ thống thông tin", 45, 12));
=======
        fetchKhoaHocThamGia();
>>>>>>> develop


        // Kiểm tra Intent để lấy từ khóa tìm kiếm từ HomeActivity
        Intent incomingIntent = getIntent();
        if (incomingIntent != null && incomingIntent.hasExtra("search_query")) {
            String searchQuery = incomingIntent.getStringExtra("search_query");
            edtTimKiem.setText(searchQuery);
            displayCourses(searchQuery); // Gọi phương thức hiển thị chung
        } else {
            displayCourses(""); // Hiển thị tất cả khóa học ban đầu
        }

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                displayCourses(s.toString()); // Gọi phương thức hiển thị chung
            }
        });

        // Gán sự kiện click cho các TextView bộ môn
        if (layoutFilterChips != null) {
            for (int i = 0; i < layoutFilterChips.getChildCount(); i++) {
                View child = layoutFilterChips.getChildAt(i);
                if (child instanceof TextView) {
                    TextView departmentChip = (TextView) child;
                    departmentChip.setOnClickListener(v -> {
                        String departmentName = departmentChip.getText().toString();
                        edtTimKiem.setText(departmentName); // Cập nhật search bar
                        displayCourses(departmentName); // Lọc theo bộ môn
                    });
                }
            }
        }


        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(XemKhoaHocActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

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
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_courses);
    }

    // Phương thức hiển thị và lọc khóa học chung
    private void displayCourses(String filterText) {
        layoutThamGia.removeAllViews();
        layoutDaLuu.removeAllViews();

        int totalFilteredCount = 0;
        String lowerCaseFilter = filterText.toLowerCase().trim();

        // Lọc và hiển thị Khóa học đã tham gia
        for (int i = 0; i < khoaHocThamGia.size(); i++) {
<<<<<<< feature/45-fix-UI
            KhoaHoc kh = khoaHocThamGia.get(i);
            if (lowerCaseFilter.isEmpty() ||
                    kh.ten.toLowerCase().contains(lowerCaseFilter) ||
                    kh.boMon.toLowerCase().contains(lowerCaseFilter)) {
                layoutThamGia.addView(createCard(kh, false, i));
                totalFilteredCount++;
            }
=======
            layoutThamGia.addView(createCard(khoaHocThamGia.get(i), true, i));
>>>>>>> develop
        }

        // Lọc và hiển thị Khóa học đã lưu
        for (int i = 0; i < khoaHocDaLuu.size(); i++) {
<<<<<<< feature/45-fix-UI
            KhoaHoc kh = khoaHocDaLuu.get(i);
            if (lowerCaseFilter.isEmpty() ||
                    kh.ten.toLowerCase().contains(lowerCaseFilter) ||
                    kh.boMon.toLowerCase().contains(lowerCaseFilter)) {
                layoutDaLuu.addView(createCard(kh, true, i));
                totalFilteredCount++;
=======
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
>>>>>>> develop
            }
        }

        // Cập nhật trạng thái "Không tìm thấy"
        if (totalFilteredCount == 0) {
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
        CardView cardView;

        if (!daLuu) {
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

        } else {
            view = LayoutInflater.from(this).inflate(R.layout.item_khoa_hoc_sv, null);
            txtTenKhoaHoc = view.findViewById(R.id.txt_ten_khoa_hoc);
            txtGiangVien = view.findViewById(R.id.txt_giang_vien);
            txtBoMon = view.findViewById(R.id.txt_bo_mon);
            Button btnSoBaiHoc = view.findViewById(R.id.btn_so_bai_hoc);
            btnSave = view.findViewById(R.id.btn_save);

            txtTenKhoaHoc.setText(kh.ten);
            txtGiangVien.setText(kh.giangVien);
            txtBoMon.setText(kh.boMon);
            btnSoBaiHoc.setText(kh.soBaiHoc + " bài học");
        }

        cardView = (CardView) view;
        int colorId = (position % 2 == 0) ? R.color.blue_3 : R.color.blue;
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, colorId));

        btnSave.setImageResource(daLuu ? R.drawable.ic_star_filled : R.drawable.ic_star_border);

        btnSave.setOnClickListener(v -> {
            if (daLuu) {
                // Giả định v.getParent().getParent().getParent() là LinearLayout wrapper
                LinearLayout parentWrapper = (LinearLayout) v.getParent().getParent().getParent();
                ((LinearLayout)parentWrapper.getParent()).removeView(parentWrapper); // Xóa khỏi layout chứa nó
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
                sessionHandler.postDelayed(sessionCheckRunnable, 10000);
            }
        };
        sessionHandler.postDelayed(sessionCheckRunnable, 10000);
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

<<<<<<< feature/45-fix-UI
        public KhoaHoc(String ten, String giangVien, String boMon, int tienDo) {
            this(ten, giangVien, boMon, tienDo, 0);
=======
        public String getDes() {
            return des;
>>>>>>> develop
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