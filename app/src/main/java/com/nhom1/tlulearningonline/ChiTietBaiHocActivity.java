package com.nhom1.tlulearningonline;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
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

import java.util.ArrayList;

public class ChiTietBaiHocActivity extends AppCompatActivity {

    private RecyclerView rvTaiLieu;
    private TaiLieuAdapter taiLieuAdapter;
    private ArrayList<String> danhSachTaiLieu;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_bai_hoc);
        // Trong HomeActivity.java và UserProfileActivity.java
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Ánh xạ view
        TextView tvTenBaiHoc = findViewById(R.id.tvTenBaiHoc);
        ImageView btnBack = findViewById(R.id.btn_back);
        TextView tvNoiDungBaiHoc = findViewById(R.id.tvNoiDungBaiHoc);
        rvTaiLieu = findViewById(R.id.rvTaiLieu);
        VideoView videoView = findViewById(R.id.videoView);


        // Lấy dữ liệu từ intent
        String lessonId = getIntent().getStringExtra("id");
        String tieuDe = getIntent().getStringExtra("tieuDe");
        String noiDung = getIntent().getStringExtra("noiDung");
        if (tieuDe != null) {
            tvTenBaiHoc.setText(tieuDe);
        }
        if (noiDung != null) {
            tvNoiDungBaiHoc.setText(noiDung);
        }
        // Sự kiện nút quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity hiện tại
            }
        });

        fetchVideoByLessonId(lessonId, videoView);
        fetchDocumentsByLessonId(lessonId);
        rvTaiLieu.setLayoutManager(new LinearLayoutManager(this));



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(ChiTietBaiHocActivity.this, HomeActivity.class); // Assuming student home is default
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish(); // Remove finish() here unless you explicitly want to remove the current activity from stack
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    Intent intent = new Intent(ChiTietBaiHocActivity.this, GroupChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    Intent intent = SessionManager.getCoursesActivityIntent(ChiTietBaiHocActivity.this); // Use helper
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(ChiTietBaiHocActivity.this, UserProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                }
                return false;
            }
        });

    }

    private void fetchVideoByLessonId(String lessonId, VideoView videoView) {
        String url = "http://14.225.207.221:6060/mobile/videos";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(url,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            if (obj.getString("lessonId").equals(lessonId)) {
                                String videoUrl = obj.getString("videoUrl");

                                Uri uri = Uri.parse(videoUrl);
                                videoView.setVideoURI(uri);
                                MediaController mediaController = new MediaController(this);
                                mediaController.setAnchorView(videoView);
                                videoView.setMediaController(mediaController);
                                videoView.start();
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    error.printStackTrace();
                }
        );

        queue.add(request);
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



}
