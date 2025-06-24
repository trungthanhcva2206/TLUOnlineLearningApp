package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView btnBack, ivUserAvatar;
    private TextView tvUserName, tvUserDepartment, tvUserEmail;
    private Button btnSettings, btnSupport, btnAboutUs, btnLogout;
    private BottomNavigationView bottomNavigationView;

    private SessionManager sessionManager;
    private Handler sessionHandler;
    private Runnable sessionCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Ánh xạ các view từ layout
        btnBack = findViewById(R.id.btn_back);
        ivUserAvatar = findViewById(R.id.iv_user_avatar);
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserDepartment = findViewById(R.id.tv_user_department);
        tvUserEmail = findViewById(R.id.tv_user_email);
        btnSettings = findViewById(R.id.btn_settings);
        btnSupport = findViewById(R.id.btn_support);
        btnAboutUs = findViewById(R.id.btn_about_us);
        btnLogout = findViewById(R.id.btn_logout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Đổ dữ liệu cứng (hard-coded) vào giao diện
        loadDummyData();
        sessionManager = new SessionManager(this);
        setupSessionCheck();

        fetchUserInfo();

        // Xử lý sự kiện click
        btnBack.setOnClickListener(v -> finish()); // Quay lại màn hình trước đó

        // Các nút chức năng hiện chỉ hiển thị Toast
        btnSettings.setOnClickListener(v -> Toast.makeText(UserProfileActivity.this, "Mở màn hình Cài đặt", Toast.LENGTH_SHORT).show());
        btnSupport.setOnClickListener(v -> Toast.makeText(UserProfileActivity.this, "Mở màn hình Hỗ trợ", Toast.LENGTH_SHORT).show());
        btnAboutUs.setOnClickListener(v -> Toast.makeText(UserProfileActivity.this, "Mở màn hình Về chúng tôi", Toast.LENGTH_SHORT).show());

        // Xử lý sự kiện cho nút Đăng xuất
        btnLogout.setOnClickListener(v -> {
            sessionManager.clearSession();
            // Hiển thị Toast thông báo
            Toast.makeText(UserProfileActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

            // Tạo Intent để quay về LoginActivity
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            // Xóa tất cả các activity trước đó khỏi stack và tạo một task mới cho LoginActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Đóng UserProfileActivity
        });

        setupBottomNavigation();
    }

    private void loadDummyData() {
        tvUserDepartment.setText("Đại học Thuỷ Lợi");
    }

    private void fetchUserInfo() {
        String userId = sessionManager.getUserId();

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy userId!", Toast.LENGTH_SHORT).show();
            sessionManager.clearSession();
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        String url = "http://14.225.207.221:6060/mobile/users/" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject user = new JSONObject(response);
                        String fullName = user.optString("fullname", "Người dùng");
                        String email = user.optString("username", "Không có username");
                        String avatarUrl = user.optString("avatar_url", "");

                        tvUserName.setText(fullName);
                        tvUserEmail.setText(email);

                        if (!avatarUrl.isEmpty()) {
                            Glide.with(this)
                                    .load(avatarUrl)
                                    .placeholder(R.drawable.ic_avatar)
                                    .error(R.drawable.ic_avatar)
                                    .circleCrop()
                                    .into(ivUserAvatar);
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
                        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
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
    private void setupSessionCheck() {
        sessionHandler = new Handler(Looper.getMainLooper());
        sessionCheckRunnable = () -> {
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(this, "Phiên đăng nhập đã hết hạn!", Toast.LENGTH_SHORT).show();
                sessionManager.clearSession();
                Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                sessionHandler.postDelayed(sessionCheckRunnable, 10000);
            }
        };
        sessionHandler.postDelayed(sessionCheckRunnable, 10000);
    }
    private void setupBottomNavigation() {
        // Đặt mục "Hồ sơ" là mục được chọn
        bottomNavigationView.setSelectedItemId(R.id.nav_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                // Chuyển về HomeActivity
                startActivity(new Intent(UserProfileActivity.this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.nav_forum) {
                // Chuyển tới GroupChatActivity
                startActivity(new Intent(UserProfileActivity.this, GroupChatActivity.class));
                return true;
            } else if (itemId == R.id.nav_courses) {
                // Chuyển tới XemKhoaHocActivity
                startActivity(new Intent(UserProfileActivity.this, XemKhoaHocActivity.class));
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Đang ở màn hình này rồi, không làm gì cả
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sessionHandler != null && sessionCheckRunnable != null) {
            sessionHandler.removeCallbacks(sessionCheckRunnable);
        }
    }
}