// FileName: ThemBaiHocActivity.java
package com.nhom1.tlulearningonline;

import android.app.Activity; // Thêm import này
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable; // Thêm import này
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ThemBaiHocActivity extends AppCompatActivity {

    private EditText edtTenBaiHoc, edtMoTaBaiHoc;
    private Button btnThemVideoTaiLieu;
    private BottomNavigationView bottomNavigationView;

    // Thêm một request code mới để nhận diện kết quả từ ThemTaiNguyenActivity
    private static final int REQUEST_THEM_TAI_NGUYEN = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_bai_hoc);

        // Ánh xạ View
        edtTenBaiHoc = findViewById(R.id.edt_ten_bai_hoc);
        edtMoTaBaiHoc = findViewById(R.id.edt_mo_ta_bai_hoc);
        btnThemVideoTaiLieu = findViewById(R.id.btn_them_video_tai_lieu);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ImageView btnBack = findViewById(R.id.btn_back);

        // Xử lý sự kiện nút quay lại
        btnBack.setOnClickListener(v -> finish());

        // Xử lý sự kiện nút "Thêm video, tài liệu"
        btnThemVideoTaiLieu.setOnClickListener(view -> {
            String tenBaiHoc = edtTenBaiHoc.getText().toString().trim();
            String moTa = edtMoTaBaiHoc.getText().toString().trim();

            if (tenBaiHoc.isEmpty()) {
                edtTenBaiHoc.setError("Vui lòng nhập tên bài học");
                return;
            }

            // Chuyển sang ThemTaiNguyenActivity và gửi dữ liệu
            Intent intent = new Intent(ThemBaiHocActivity.this, ThemTaiNguyenActivity.class);
            intent.putExtra("tenBaiHoc", tenBaiHoc);
            intent.putExtra("moTa", moTa);

            // Sửa: Dùng startActivityForResult thay vì startActivity
            startActivityForResult(intent, REQUEST_THEM_TAI_NGUYEN);
        });

        // Xử lý Bottom Navigation View
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(ThemBaiHocActivity.this, HomeGVActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    Intent intent = new Intent(ThemBaiHocActivity.this, GroupChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    Intent intent = SessionManager.getCoursesActivityIntent(ThemBaiHocActivity.this);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(ThemBaiHocActivity.this, UserProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    // Thêm toàn bộ phương thức onActivityResult vào ThemBaiHocActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra xem đây có phải là kết quả trả về từ ThemTaiNguyenActivity không
        if (requestCode == REQUEST_THEM_TAI_NGUYEN && resultCode == Activity.RESULT_OK && data != null) {
            // Nhận được kết quả thành công, giờ đóng activity này
            // và chuyển tiếp kết quả (data) về cho SuaKhoaHocActivity.
            setResult(Activity.RESULT_OK, data);
            finish();
        }
    }
}