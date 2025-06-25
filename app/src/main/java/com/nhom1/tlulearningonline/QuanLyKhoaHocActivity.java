package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

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

public class QuanLyKhoaHocActivity extends AppCompatActivity {

    private static final int REQUEST_TAO_KHOA_HOC = 100;
    private static final int REQUEST_SUA_KHOA_HOC = 200;

    private LinearLayout layoutDsKhoaHoc;
    private final ArrayList<KhoaHoc> danhSachDayDu = new ArrayList<>();
    private int viTriDangSua = -1;

    private EditText edtTimKiem;
    TextView tv_ten_nguoi_dung;
    ImageView iv_avatar;

    private BottomNavigationView bottomNavigationView; // Declared here

    private Handler sessionHandler;
    private Runnable sessionCheckRunnable;

    private void fetchUserInfo() {
        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getUserId();

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy userId!", Toast.LENGTH_SHORT).show();
            sessionManager.clearSession();
            Intent intent = new Intent(QuanLyKhoaHocActivity.this, LoginActivity.class);
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
                        Intent intent = new Intent(QuanLyKhoaHocActivity.this, LoginActivity.class);
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
    protected void onResume() {
        super.onResume();
        loadKhoaHocFromApi();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_khoa_hoc);

        // Initialize bottomNavigationView here
        tv_ten_nguoi_dung = findViewById(R.id.tv_ten_nguoi_dung);
        iv_avatar = findViewById(R.id.iv_avatar);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        edtTimKiem = findViewById(R.id.edt_tim_kiem);

        layoutDsKhoaHoc = findViewById(R.id.layout_ds_khoa_hoc);
        Button btnTaoKhoaHoc = findViewById(R.id.btn_tao_khoa_hoc);

        fetchUserInfo();
        setupSessionCheck();

        // Dữ liệu mẫu
        loadKhoaHocFromApi();

        iv_avatar.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLyKhoaHocActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        btnTaoKhoaHoc.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLyKhoaHocActivity.this, TaoKhoaHocActivity.class);
            startActivityForResult(intent, REQUEST_TAO_KHOA_HOC);
        });

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // THAY ĐỔI LOGIC: Gọi hàm tìm kiếm
                timKiemKhoaHoc(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
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

    // MỚI: Hàm tìm kiếm theo logic của XemKhoaHocActivity
    private void timKiemKhoaHoc(String tuKhoa) {
        ArrayList<KhoaHoc> danhSachDaLoc = new ArrayList<>();
        String lowerCaseTuKhoa = tuKhoa.toLowerCase();

        if (tuKhoa.isEmpty()) {
            danhSachDaLoc.addAll(danhSachDayDu);
        } else {
            for (KhoaHoc kh : danhSachDayDu) {
                if (kh.ten.toLowerCase().contains(lowerCaseTuKhoa) || kh.moTa.toLowerCase().contains(lowerCaseTuKhoa)) {
                    danhSachDaLoc.add(kh);
                }
            }
        }
        // Gọi hàm hiển thị với danh sách đã lọc
        hienThiDanhSach(danhSachDaLoc);
    }

    // MỚI: Hàm hiển thị theo logic của XemKhoaHocActivity
    private void hienThiDanhSach(ArrayList<KhoaHoc> danhSachCanHienThi) {
        layoutDsKhoaHoc.removeAllViews();
        for (int i = 0; i < danhSachCanHienThi.size(); i++) {
            View itemView = createKhoaHocView(danhSachCanHienThi.get(i), i);
            layoutDsKhoaHoc.addView(itemView);
        }
    }

    // Đổi tên từ addKhoaHocToLayout thành createKhoaHocView để logic rõ ràng hơn
    private View createKhoaHocView(KhoaHoc kh, int position) {
        View itemView = LayoutInflater.from(this).inflate(R.layout.item_khoa_hoc, layoutDsKhoaHoc, false);
        CardView cardView = (CardView) itemView;

        int colorId = (position % 2 == 0) ? R.color.blue_3 : R.color.blue;
        cardView.setCardBackgroundColor(ContextCompat.getColor(this, colorId));

        CustomViewBinder.bind(itemView, kh);

        ImageView btnEdit = itemView.findViewById(R.id.btn_edit);


        btnEdit.setOnClickListener(v -> {
            viTriDangSua = position;
            Intent intent = new Intent(QuanLyKhoaHocActivity.this, SuaKhoaHocActivity.class);
            intent.putExtra("course_id", kh.id); // truyền id
            startActivityForResult(intent, REQUEST_SUA_KHOA_HOC);
        });

        itemView.setOnClickListener(v -> {
            Intent intent = new Intent(QuanLyKhoaHocActivity.this, ChiTietKhoaHocActivity.class);
            intent.putExtra("course_id", kh.id);
            intent.putExtra("tieu_de", kh.ten);
            intent.putExtra("des", kh.moTa);
            intent.putExtra("tac_gia", kh.tenGV);
            intent.putExtra("so_bai", kh.dsBaiHoc.size());
            startActivity(intent);
        });

        return itemView;
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

    private void setupSessionCheck() {
        sessionHandler = new Handler(Looper.getMainLooper());
        sessionCheckRunnable = () -> {
            SessionManager sessionManager = new SessionManager(QuanLyKhoaHocActivity.this);
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(QuanLyKhoaHocActivity.this, "Phiên đăng nhập đã hết hạn!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(QuanLyKhoaHocActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                sessionHandler.postDelayed(sessionCheckRunnable, 10000); // 10 giây kiểm tra lại
            }
        };
        sessionHandler.postDelayed(sessionCheckRunnable, 10000); // bắt đầu sau 10 giây
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
                KhoaHoc khoaHocMoi = new KhoaHoc("0", ten, moTa, "GV. Nguyễn Văn A", dsBaiHoc);
                danhSachDayDu.add(khoaHocMoi);
                createKhoaHocView(khoaHocMoi, danhSachDayDu.size() - 1);
            } else if (requestCode == REQUEST_SUA_KHOA_HOC && data.getBooleanExtra("course_updated", false)) {
                loadKhoaHocFromApi();
            }
        }
    }
    public static class KhoaHoc {
        String id;
        String ten, moTa, tenGV;
        ArrayList<String> dsBaiHoc;

        public KhoaHoc(String id, String ten, String moTa, String tenGV, ArrayList<String> dsBaiHoc) {
            this.id = id;
            this.ten = ten;
            this.moTa = moTa;
            this.tenGV = tenGV;
            this.dsBaiHoc = dsBaiHoc != null ? dsBaiHoc : new ArrayList<>();
        }

        public int getSoBaiHoc() {
            return dsBaiHoc.size();
        }
    }

    private void loadKhoaHocFromApi() {
        String urlCourses = "http://14.225.207.221:6060/mobile/courses";
        String urlLessons = "http://14.225.207.221:6060/mobile/lessons";

        RequestQueue queue = Volley.newRequestQueue(this);

        // Lấy userId từ session
        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getUserId();
        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy userId!", Toast.LENGTH_SHORT).show();
            return;
        }

        // B1: Lấy toàn bộ lessons
        Utf8StringRequest  lessonRequest = new Utf8StringRequest (Request.Method.GET, urlLessons,
                response -> {
                    try {
                        HashMap<String, ArrayList<String>> lessonsMap = new HashMap<>();
                        org.json.JSONArray lessonsArray = new org.json.JSONArray(response);
                        for (int i = 0; i < lessonsArray.length(); i++) {
                            JSONObject lesson = lessonsArray.getJSONObject(i);
                            String courseId = lesson.getString("courseId");
                            String lessonTitle = lesson.getString("title");
                            lessonsMap.computeIfAbsent(courseId, k -> new ArrayList<>()).add(lessonTitle);
                        }

                        // B2: Lấy danh sách khóa học
                        Utf8StringRequest  courseRequest = new Utf8StringRequest (Request.Method.GET, urlCourses,
                                courseResponse -> {
                                    try {
                                        danhSachDayDu.clear();
                                        layoutDsKhoaHoc.removeAllViews();

                                        org.json.JSONArray courseArray = new org.json.JSONArray(courseResponse);
                                        for (int i = 0; i < courseArray.length(); i++) {
                                            JSONObject obj = courseArray.getJSONObject(i);
                                            String teacherId = obj.getString("teacherId");

                                            if (!userId.equals(teacherId)) continue;

                                            String id = obj.getString("id");
                                            String title = obj.getString("title");
                                            String description = obj.getString("description");
                                            String teacher = obj.getString("teacherName");

                                            ArrayList<String> lessons = lessonsMap.getOrDefault(id, new ArrayList<>());

                                            KhoaHoc kh = new KhoaHoc(id, title, description, teacher, lessons);
                                            danhSachDayDu.add(kh);
                                        }
                                        // THAY ĐỔI LOGIC: Hiển thị toàn bộ danh sách ban đầu
                                        hienThiDanhSach(danhSachDayDu);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Toast.makeText(this, "Lỗi xử lý dữ liệu khóa học!", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                error -> {
                                    error.printStackTrace();
                                    Toast.makeText(this, "Lỗi tải danh sách khóa học!", Toast.LENGTH_SHORT).show();
                                }
                        );
                        queue.add(courseRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý dữ liệu bài học!", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Lỗi tải danh sách bài học!", Toast.LENGTH_SHORT).show();
                }
        );
        queue.add(lessonRequest);
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