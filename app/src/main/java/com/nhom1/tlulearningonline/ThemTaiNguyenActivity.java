// FileName: ThemTaiNguyenActivity.java
package com.nhom1.tlulearningonline;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ThemTaiNguyenActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO = 100;
    private static final int REQUEST_TAILIEU = 101;

    private Button btnThemVideo, btnThemTaiLieu, btnThemBaiHoc;
    private LinearLayout layoutDsVideo, layoutDsPdf;
    private BottomNavigationView bottomNavigationView;

    private final List<Uri> danhSachVideo = new ArrayList<>();
    private final List<Uri> danhSachTaiLieu = new ArrayList<>();

    private String tenBaiHoc;
    private String moTaBaiHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tai_nguyen);

        // Nhận dữ liệu từ ThemBaiHocActivity
        Intent intent = getIntent();
        tenBaiHoc = intent.getStringExtra("tenBaiHoc");
        moTaBaiHoc = intent.getStringExtra("moTa");

        // Ánh xạ Views từ layout activity_them_tai_nguyen.xml
        btnThemVideo = findViewById(R.id.btn_them_video);
        btnThemTaiLieu = findViewById(R.id.btn_them_tai_lieu);
        btnThemBaiHoc = findViewById(R.id.btn_them_bai_hoc);
        layoutDsVideo = findViewById(R.id.layout_ds_video);
        layoutDsPdf = findViewById(R.id.layout_ds_pdf);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ImageView btnBack = findViewById(R.id.btn_back);

        btnBack.setOnClickListener(v -> finish());

        // Sự kiện chọn video
        btnThemVideo.setOnClickListener(v -> {
            Intent videoIntent = new Intent(Intent.ACTION_GET_CONTENT);
            videoIntent.setType("video/*");
            startActivityForResult(Intent.createChooser(videoIntent, "Chọn video"), REQUEST_VIDEO);
        });

        // Sự kiện chọn tài liệu
        btnThemTaiLieu.setOnClickListener(v -> {
            Intent taiLieuIntent = new Intent(Intent.ACTION_GET_CONTENT);
            taiLieuIntent.setType("application/pdf");
            startActivityForResult(Intent.createChooser(taiLieuIntent, "Chọn tài liệu"), REQUEST_TAILIEU);
        });

        // Sự kiện nút "Thêm bài học" (nút cuối cùng)
        btnThemBaiHoc.setOnClickListener(v -> {
            if (danhSachVideo.isEmpty()) {
                Toast.makeText(this, "Vui lòng thêm ít nhất 1 video", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Tại đây, bạn sẽ thực hiện logic lưu trữ cuối cùng
            // ví dụ: tải video/tài liệu lên server, lưu thông tin bài học vào database.
            // Dưới đây là ví dụ hiển thị thông tin bằng Toast và trả về kết quả.

            StringBuilder tenTaiLieuBuilder = new StringBuilder();
            for (Uri uri : danhSachTaiLieu) {
                String fileName = getFileName(uri);
                if (!fileName.isEmpty()) {
                    tenTaiLieuBuilder.append("- ").append(fileName).append("\n");
                }
            }

            String message = "Đã sẵn sàng để thêm bài học:\n" +
                    "Tên: " + tenBaiHoc + "\n" +
                    "Mô tả: " + moTaBaiHoc + "\n" +
                    "Số video: " + danhSachVideo.size() + "\n" +
                    "Tài liệu: \n" + tenTaiLieuBuilder.toString();

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();


            // Nếu bạn cần trả kết quả về màn hình tạo khóa học (trước cả màn thêm bài học)
            // thì bạn cần dùng startActivityForResult từ màn đó để gọi ThemBaiHocActivity.
            // Sau đó, khi logic ở đây hoàn tất, bạn có thể đặt kết quả và kết thúc cả 2 activity.

            // Ví dụ: tạo intent kết quả
            Intent resultIntent = new Intent();
            resultIntent.putExtra("tenBaiHoc", tenBaiHoc);
            resultIntent.putExtra("moTaBaiHoc", moTaBaiHoc);
            resultIntent.putParcelableArrayListExtra("danhSachVideo", (ArrayList<Uri>) danhSachVideo);
            resultIntent.putParcelableArrayListExtra("danhSachTaiLieu", (ArrayList<Uri>) danhSachTaiLieu);
            setResult(Activity.RESULT_OK, resultIntent);

            // Kết thúc activity hiện tại để quay về màn hình trước đó.
            finish();
        });

        // Xử lý Bottom Navigation View
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Intent homeIntent = new Intent(ThemTaiNguyenActivity.this, HomeGVActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.nav_forum) {
                Intent forumIntent = new Intent(ThemTaiNguyenActivity.this, GroupChatActivity.class);
                forumIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(forumIntent);
                return true;
            } else if (itemId == R.id.nav_courses) {
                Intent coursesIntent = SessionManager.getCoursesActivityIntent(ThemTaiNguyenActivity.this);
                coursesIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(coursesIntent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                Intent profileIntent = new Intent(ThemTaiNguyenActivity.this, UserProfileActivity.class);
                profileIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri == null) return;

            LayoutInflater inflater = LayoutInflater.from(this);
            String fileName = getFileName(uri);

            if (requestCode == REQUEST_VIDEO) {
                danhSachVideo.add(uri);
                View itemView = inflater.inflate(R.layout.item_video, layoutDsVideo, false);
                TextView tvTen = itemView.findViewById(R.id.tvTenVideo);
                ImageView btnXoa = itemView.findViewById(R.id.btnXoaVideo);
                tvTen.setText(fileName);

                btnXoa.setOnClickListener(v -> {
                    layoutDsVideo.removeView(itemView);
                    danhSachVideo.remove(uri);
                });
                layoutDsVideo.addView(itemView);

            } else if (requestCode == REQUEST_TAILIEU) {
                danhSachTaiLieu.add(uri);
                View itemView = inflater.inflate(R.layout.item_pdf, layoutDsPdf, false);
                TextView tvTen = itemView.findViewById(R.id.tvTenPDF);
                ImageView btnXoa = itemView.findViewById(R.id.btnXoaPDF);
                tvTen.setText(fileName);

                btnXoa.setOnClickListener(v -> {
                    layoutDsPdf.removeView(itemView);
                    danhSachTaiLieu.remove(uri);
                });
                layoutDsPdf.addView(itemView);
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = "";
        try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex >= 0) {
                    result = cursor.getString(nameIndex);
                }
            }
        }
        return result != null ? result : "";
    }
}