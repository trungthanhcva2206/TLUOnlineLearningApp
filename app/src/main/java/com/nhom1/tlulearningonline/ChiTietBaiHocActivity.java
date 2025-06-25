package com.nhom1.tlulearningonline;


import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class ChiTietBaiHocActivity extends AppCompatActivity {

    private RecyclerView rvTaiLieu;
    private TaiLieuAdapter taiLieuAdapter;
    private ArrayList<String> danhSachTaiLieu;
    private BottomNavigationView bottomNavigationView;

    // === BIẾN MỚI CHO CHỨC NĂNG LƯU VIDEO ===
    private ImageView btnSave;
    private VideoView videoView;
    private VideoDbHelper dbHelper;
    private String lessonId;
    private String videoUrlFromServer;
    private long downloadID;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadID == id) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                try (android.database.Cursor cursor = dm.query(query)) {
                    if (cursor.moveToFirst()) {
                        int statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(statusIndex)) {
                            int uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                            String savedUriString = cursor.getString(uriIndex);
                            dbHelper.addVideo(lessonId, savedUriString);
                            Toast.makeText(ChiTietBaiHocActivity.this, "Tải video thành công!", Toast.LENGTH_SHORT).show();
                            btnSave.setVisibility(View.GONE); // Ẩn nút lưu sau khi tải xong
                        } else {
                            Toast.makeText(ChiTietBaiHocActivity.this, "Tải video thất bại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
    };
    // ===========================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_bai_hoc);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Ánh xạ view
        TextView tvTenBaiHoc = findViewById(R.id.tvTenBaiHoc);
        ImageView btnBack = findViewById(R.id.btn_back);
        TextView tvNoiDungBaiHoc = findViewById(R.id.tvNoiDungBaiHoc);
        rvTaiLieu = findViewById(R.id.rvTaiLieu);
        videoView = findViewById(R.id.videoView);
        btnSave = findViewById(R.id.btn_save); // Ánh xạ nút lưu mới

        // Khởi tạo DB Helper
        dbHelper = new VideoDbHelper(this);
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));


        // Lấy dữ liệu từ intent
        lessonId = getIntent().getStringExtra("id");
        String tieuDe = getIntent().getStringExtra("tieuDe");
        String noiDung = getIntent().getStringExtra("noiDung");

        if (tieuDe != null) tvTenBaiHoc.setText(tieuDe);
        if (noiDung != null) tvNoiDungBaiHoc.setText(noiDung);

        btnBack.setOnClickListener(v -> finish());

        // === LOGIC MỚI: KIỂM TRA VIDEO LOCAL TRƯỚC ===
        String localVideoPath = dbHelper.getLocalVideoPath(lessonId);
        if (localVideoPath != null) {
            // Đã có video local -> phát video
            Toast.makeText(this, "Phát video từ bộ nhớ local.", Toast.LENGTH_SHORT).show();
            playVideoFromUri(Uri.parse(localVideoPath));
            btnSave.setVisibility(View.GONE); // Ẩn nút lưu vì đã có
        } else {
            // Chưa có video local -> lấy từ server
            fetchVideoByLessonId(lessonId);
        }
        // ============================================

        fetchDocumentsByLessonId(lessonId);
        rvTaiLieu.setLayoutManager(new LinearLayoutManager(this));
        setupBottomNavigation();

        // Sự kiện click cho nút lưu
        btnSave.setOnClickListener(v -> downloadVideo());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký receiver để tránh rò rỉ bộ nhớ
        unregisterReceiver(onDownloadComplete);
    }

    private void playVideoFromUri(Uri uri) {
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
    }


    private void fetchVideoByLessonId(String lessonId) {
        String url = "http://14.225.207.221:6060/mobile/videos";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            if (obj.getString("lessonId").equals(lessonId)) {
                                videoUrlFromServer = obj.getString("videoUrl");
                                Uri uri = Uri.parse(videoUrlFromServer);
                                playVideoFromUri(uri);
                                btnSave.setVisibility(View.VISIBLE); // Hiển thị nút lưu
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Không thể tải dữ liệu video", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }

    private void downloadVideo() {
        if (videoUrlFromServer == null || videoUrlFromServer.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy URL của video.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Bắt đầu tải video...", Toast.LENGTH_SHORT).show();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoUrlFromServer));
        request.setTitle("Tải video bài học " + lessonId);
        request.setDescription("Đang tải xuống...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Lưu file vào thư mục Download của ứng dụng
        String fileName = "lesson_" + lessonId + ".mp4";
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadID = manager.enqueue(request);
    }


    private void fetchDocumentsByLessonId(String lessonId) {
        String url = "http://14.225.207.221:6060/mobile/documents";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {
                    ArrayList<Document> docs = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            if (obj.getString("lessonId").equals(lessonId)) {
                                Document doc = new Document(
                                        obj.getString("id"),
                                        obj.getString("title"),
                                        obj.getString("fileUrl"),
                                        lessonId
                                );
                                docs.add(doc);
                                Log.d("DOC_LOG", "Tài liệu: " + doc.getName());
                            }
                        } catch (Exception e) {
                            Log.e("DOC_LOG", "Lỗi parse JSON: " + e.getMessage());
                        }
                    }

                    Log.d("DOC_LOG", "Tổng số tài liệu tìm được: " + docs.size());

                    taiLieuAdapter = new TaiLieuAdapter(docs, this);
                    rvTaiLieu.setAdapter(taiLieuAdapter);
                },
                error -> Log.e("DOC_LOG", "Lỗi gọi API: " + error.toString())
        );

        queue.add(request);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivityWithFlags(HomeActivity.class);
                return true;
            } else if (itemId == R.id.nav_forum) {
                startActivityWithFlags(GroupChatActivity.class);
                return true;
            } else if (itemId == R.id.nav_courses) {
                Intent intent = SessionManager.getCoursesActivityIntent(this);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return true;
            } else if (itemId == R.id.nav_profile) {
                startActivityWithFlags(UserProfileActivity.class);
                return true;
            }
            return false;
        });
    }

    private void startActivityWithFlags(Class<?> cls) {
        Intent intent = new Intent(ChiTietBaiHocActivity.this, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}