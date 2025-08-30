package com.nhom1.tlulearningonline;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChiTietKhoaHocActivity extends AppCompatActivity {
    TextView tvTieuDe, tvSoBai, tvTacGia, tvMoTa;
    RecyclerView recyclerView;
    ImageView btnBack;

    private BottomNavigationView bottomNavigationView;
    private BaiHocAdapter adapter;
    private List<Lesson> lessonList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_khoa_hoc);

        // Trong HomeActivity.java và UserProfileActivity.java
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Ánh xạ
        tvTieuDe = findViewById(R.id.tv_course_name);
        tvSoBai = findViewById(R.id.tv_lesson_count);
        tvTacGia = findViewById(R.id.tv_teacher);
        tvMoTa = findViewById(R.id.tv_description);
        recyclerView = findViewById(R.id.recycler_lessons);
        btnBack = findViewById(R.id.btn_back);

        // Nhận dữ liệu từ intent
        Intent intent = getIntent();
        String courseId = intent.getStringExtra("course_id");
        String tieuDe = intent.getStringExtra("tieu_de");
        String moTa = intent.getStringExtra("des");
        String tacGia = intent.getStringExtra("tac_gia");
        int soBai = intent.getIntExtra("so_bai", 0);
        adapter = new BaiHocAdapter(lessonList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        fetchLessonsByCourse(courseId);
        // Hiển thị thông tin khóa học
        tvTieuDe.setText(tieuDe);
        tvMoTa.setText(moTa);
        tvTacGia.setText("👩‍🏫 " + tacGia);
        tvSoBai.setText("📘 " + soBai + " bài học");



        // Xử lý nút back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng activity hiện tại và quay lại
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(ChiTietKhoaHocActivity.this, HomeActivity.class); // Assuming student home is default
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish(); // Remove finish() here unless you explicitly want to remove the current activity from stack
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    Intent intent = new Intent(ChiTietKhoaHocActivity.this, GroupChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    Intent intent = SessionManager.getCoursesActivityIntent(ChiTietKhoaHocActivity.this); // Use helper
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(ChiTietKhoaHocActivity.this, UserProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                }
                return false;
            }
        });

    }

    private void fetchLessonsByCourse(String courseId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://14.225.207.221:6060/mobile/lessons";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    lessonList.clear(); // Xóa dữ liệu cũ

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            if (obj.getString("courseId").equals(courseId)) {
                                String title = obj.getString("title");
                                String content = obj.getString("content");

                                Log.d("ChiTietKhoaHoc", "Thêm bài học: " + title);

                                lessonList.add(new Lesson(
                                        obj.getString("id"),
                                        title,
                                        content,
                                        courseId
                                ));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    adapter.notifyDataSetChanged(); // Cập nhật lại RecyclerView
                },
                error -> {
                    Toast.makeText(this, "Lỗi khi tải bài học", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(jsonArrayRequest);
    }


}