package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView btnBack, ivUserAvatar;
    private TextView tvUserName, tvUserDepartment, tvUserEmail;
    private Button btnSettings, btnSupport, btnAboutUs, btnLogout;
    private BottomNavigationView bottomNavigationView;

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

        // Xử lý sự kiện click
        btnBack.setOnClickListener(v -> finish()); // Quay lại màn hình trước đó

        // Các nút chức năng hiện chỉ hiển thị Toast
        btnSettings.setOnClickListener(v -> Toast.makeText(UserProfileActivity.this, "Mở màn hình Cài đặt", Toast.LENGTH_SHORT).show());
        btnSupport.setOnClickListener(v -> Toast.makeText(UserProfileActivity.this, "Mở màn hình Hỗ trợ", Toast.LENGTH_SHORT).show());
        btnAboutUs.setOnClickListener(v -> Toast.makeText(UserProfileActivity.this, "Mở màn hình Về chúng tôi", Toast.LENGTH_SHORT).show());

        // Xử lý sự kiện cho nút Đăng xuất
        btnLogout.setOnClickListener(v -> {
            // Hiển thị Toast thông báo
            Toast.makeText(UserProfileActivity.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();

            // Tạo Intent để quay về LoginActivity
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            // Xóa tất cả các activity trước đó khỏi stack và tạo một task mới cho LoginActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish(); // Đóng UserProfileActivity
        });

        // Xử lý thanh điều hướng dưới cùng
        setupBottomNavigation();
    }

    private void loadDummyData() {
        // Đây là nơi bạn sẽ đổ dữ liệu người dùng thật vào sau này.
        // Hiện tại, chúng ta dùng dữ liệu cứng như trong ảnh mẫu.
        tvUserName.setText("Nguyễn Thị Phương Anh");
        tvUserDepartment.setText("Hệ thống thông tin");
        tvUserEmail.setText("2251161942@e.tlu.edu.vn");
        // Bạn có thể thay đổi ảnh đại diện nếu muốn
        // ivUserAvatar.setImageResource(R.drawable.your_avatar_image);
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
}