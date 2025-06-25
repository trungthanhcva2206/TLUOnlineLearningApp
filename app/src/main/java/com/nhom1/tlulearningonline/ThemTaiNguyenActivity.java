// FileName: ThemTaiNguyenActivity.java
package com.nhom1.tlulearningonline;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
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

import com.android.volley.Request;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThemTaiNguyenActivity extends AppCompatActivity {

    private static final int REQUEST_VIDEO = 100;
    private static final int REQUEST_TAILIEU = 101;

    private Button btnThemVideo, btnThemTaiLieu, btnThemBaiHoc;
    private LinearLayout layoutDsVideo, layoutDsPdf;
    private BottomNavigationView bottomNavigationView;

    private final List<Uri> danhSachVideo = new ArrayList<>();
    private final List<Uri> danhSachTaiLieu = new ArrayList<>();
    private String lessonId;

    private String tenBaiHoc;
    private String moTaBaiHoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_tai_nguyen);

        lessonId = getIntent().getStringExtra("lesson_id");

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

            // Upload tất cả video
            for (Uri videoUri : danhSachVideo) {
                uploadFile(videoUri, "http://14.225.207.221:6060/mobile/videos/upload", "video");
            }

            // Upload tất cả tài liệu PDF (nếu có)
            for (Uri pdfUri : danhSachTaiLieu) {
                uploadFile(pdfUri, "http://14.225.207.221:6060/mobile/documents/upload", "pdf");
            }

            // Hiển thị thông tin ra Toast
            StringBuilder tenTaiLieuBuilder = new StringBuilder();
            for (Uri uri : danhSachTaiLieu) {
                String fileName = getFileName(uri);
                if (!fileName.isEmpty()) {
                    tenTaiLieuBuilder.append("- ").append(fileName).append("\n");
                }
            }

            String message = "Đang tải lên bài học:\n" +
                    "Tên: " + tenBaiHoc + "\n" +
                    "Mô tả: " + moTaBaiHoc + "\n" +
                    "Số video: " + danhSachVideo.size() + "\n" +
                    "Tài liệu: \n" + tenTaiLieuBuilder;

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();

            // (Tuỳ chọn) Trả kết quả về activity trước đó
            Intent resultIntent = new Intent();
            resultIntent.putExtra("tenBaiHoc", tenBaiHoc);
            resultIntent.putExtra("moTaBaiHoc", moTaBaiHoc);
            resultIntent.putParcelableArrayListExtra("danhSachVideo", (ArrayList<Uri>) danhSachVideo);
            resultIntent.putParcelableArrayListExtra("danhSachTaiLieu", (ArrayList<Uri>) danhSachTaiLieu);
            setResult(Activity.RESULT_OK, resultIntent);

            // Kết thúc activity
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
    private byte[] readFileBytes(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            int nRead;
            byte[] data = new byte[4096];
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    private void uploadFile(Uri fileUri, String url, String fileType) {
        String fileName = getFileName(fileUri);
        byte[] fileBytes = readFileBytes(fileUri);

        if (fileBytes == null) {
            Toast.makeText(this, "Không đọc được file " + fileName, Toast.LENGTH_SHORT).show();
            Log.e("UPLOAD_FAIL", "File bytes null for " + fileName);
            return;
        }

        // Log kích thước file
        Log.d("UPLOAD_INFO", "Uploading: " + fileName + " (" + fileBytes.length / 1024 + " KB)");
        Toast.makeText(this, "Đang tải lên: " + fileName, Toast.LENGTH_SHORT).show();

        // Lấy kiểu MIME từ hệ thống (nếu có)
        String mediaType = getContentResolver().getType(fileUri);
        if (mediaType == null) {
            mediaType = fileType.equals("video") ? "video/mp4" : "application/pdf";
        }

        // Key cho file upload (backend yêu cầu là 'file' cho tài liệu)
        String fileKey = fileType.equals("video") ? "video" : "file";

        // Tạo Multipart request
        okhttp3.RequestBody requestBody = new okhttp3.MultipartBody.Builder()
                .setType(okhttp3.MultipartBody.FORM)
                .addFormDataPart("title", tenBaiHoc)
                .addFormDataPart("lessonId", lessonId)
                .addFormDataPart(fileKey, fileName,
                        okhttp3.RequestBody.create(fileBytes, okhttp3.MediaType.parse(mediaType)))
                .build();

        // OkHttp client với timeout dài hơn
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder()
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        // Tạo request
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)  // VD: http://14.225.207.221:6060/mobile/documents/upload
                .post(requestBody)
                .build();

        // Gửi request bất đồng bộ
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(ThemTaiNguyenActivity.this, "Lỗi tải lên: " + fileName, Toast.LENGTH_SHORT).show();
                    Log.e("UPLOAD_FAIL", "File: " + fileName + " | Error: " + e.getMessage(), e);
                });
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                String resBody = response.body() != null ? response.body().string() : "No Response Body";

                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(ThemTaiNguyenActivity.this, "Tải lên thành công: " + fileName, Toast.LENGTH_SHORT).show();
                        Log.d("UPLOAD_SUCCESS", "File: " + fileName + " | Response: " + resBody);
                    } else {
                        Toast.makeText(ThemTaiNguyenActivity.this, "Tải lên thất bại: " + fileName, Toast.LENGTH_SHORT).show();
                        Log.e("UPLOAD_ERROR", "Code: " + response.code() + " | File: " + fileName + " | Response: " + resBody);
                    }
                });
            }
        });
    }




}