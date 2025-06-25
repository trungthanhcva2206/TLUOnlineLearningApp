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
import android.widget.EditText;
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

public class ThemBaiHocActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO = 100;
    private static final int REQUEST_TAILIEU = 101;

    private EditText edtTenBaiHoc;
    private Button btnThemVideo, btnThemTaiLieu, btnThemBaiHoc;
    private LinearLayout layoutDsVideo, layoutDsPdf;
    private BottomNavigationView bottomNavigationView;

    private final List<Uri> danhSachVideo = new ArrayList<>();
    private final List<Uri> danhSachTaiLieu = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_bai_hoc);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        edtTenBaiHoc = findViewById(R.id.edt_ten_bai_hoc);
        btnThemVideo = findViewById(R.id.btn_them_video);
        btnThemTaiLieu = findViewById(R.id.btn_them_tai_lieu);
        btnThemBaiHoc = findViewById(R.id.btn_them_bai_hoc);
        layoutDsVideo = findViewById(R.id.layout_ds_video);
        layoutDsPdf = findViewById(R.id.layout_ds_pdf);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        btnThemVideo.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("video/*");
            startActivityForResult(Intent.createChooser(intent, "Chọn video"), REQUEST_VIDEO);
        });

        btnThemTaiLieu.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("application/pdf");
            startActivityForResult(Intent.createChooser(intent, "Chọn tài liệu"), REQUEST_TAILIEU);
        });

        btnThemBaiHoc.setOnClickListener(v -> {
            String tenBaiHoc = edtTenBaiHoc.getText().toString().trim();

            if (tenBaiHoc.isEmpty()) {
                edtTenBaiHoc.setError("Vui lòng nhập tên bài học");
                return;
            }

            if (danhSachVideo.isEmpty()) {
                Toast.makeText(this, "Vui lòng thêm ít nhất 1 video", Toast.LENGTH_SHORT).show();
                return;
            }

            // Trả dữ liệu về cho màn Tạo khóa học
            Intent resultIntent = new Intent();
            resultIntent.putExtra("tenBaiHoc", tenBaiHoc);
            resultIntent.putExtra("videoUri", danhSachVideo.get(0).toString()); // Gửi video đầu tiên

            StringBuilder tenTaiLieu = new StringBuilder();
            for (Uri uri : danhSachTaiLieu) {
                String fileName = getFileName(uri);
                if (!fileName.isEmpty()) {
                    tenTaiLieu.append("- ").append(fileName).append("\n");
                }
            }

            resultIntent.putExtra("tenTaiLieu", tenTaiLieu.toString());
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(ThemBaiHocActivity.this, HomeGVActivity.class); // Assuming student home is default
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish(); // Remove finish() here unless you explicitly want to remove the current activity from stack
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    Intent intent = new Intent(ThemBaiHocActivity.this, GroupChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    Intent intent = SessionManager.getCoursesActivityIntent(ThemBaiHocActivity.this); // Use helper
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(ThemBaiHocActivity.this, UserProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                }
                return false;
            }
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
