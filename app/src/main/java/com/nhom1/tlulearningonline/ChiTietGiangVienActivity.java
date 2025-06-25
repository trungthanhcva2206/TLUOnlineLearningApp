package com.nhom1.tlulearningonline;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nhom1.tlulearningonline.KhoaHocAdapter;
import com.nhom1.tlulearningonline.KhoaHoc;

import org.json.JSONException;
import org.json.JSONObject;

public class ChiTietGiangVienActivity extends AppCompatActivity {

    private ImageView btnBack;
    private TextView tvTenGv, tvBoMon, tvEmail;
    private RecyclerView recyclerKhoaHoc;
    private KhoaHocAdapter adapter;
    private List<KhoaHoc> khoaHocList;
    private ImageView ivAvatar;

    private BottomNavigationView bottomNavigationView;

    private Handler sessionHandler;
    private Runnable sessionCheckRunnable;

    private void fetchUserInfo() {
        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getUserId();

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy userId!", Toast.LENGTH_SHORT).show();
            sessionManager.clearSession();
            Intent intent = new Intent(ChiTietGiangVienActivity.this, LoginActivity.class);
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
                        String avatarUrl = user.optString("avatar_url", "");


                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            Log.d("AvatarURL", avatarUrl);
                            Glide.with(this)
                                    .load(avatarUrl)
                                    .placeholder(R.drawable.ic_avatar)
                                    .error(R.drawable.ic_avatar)
                                    .circleCrop()
                                    .into(ivAvatar);
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
                        Intent intent = new Intent(ChiTietGiangVienActivity.this, LoginActivity.class);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_giang_vien);

        // Trong HomeActivity.java và UserProfileActivity.java
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        btnBack = findViewById(R.id.btn_back);
        tvTenGv = findViewById(R.id.tv_ten_gv);
        tvBoMon = findViewById(R.id.tv_bo_mon);
        tvEmail = findViewById(R.id.tv_email);
        recyclerKhoaHoc = findViewById(R.id.recycler_khoa_hoc);
        ivAvatar = findViewById(R.id.iv_avatar);

        fetchUserInfo();
        setupSessionCheck();

        Intent intent = getIntent();
        String ten = intent.getStringExtra("ten");
        String avatarUrl = intent.getStringExtra("avatarResId");

        Glide.with(this)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_avatar)
                .error(R.drawable.ic_avatar)
                .circleCrop()
                .into((ImageView) findViewById(R.id.img_giang_vien));

        tvTenGv.setText(ten);
        tvEmail.setText(ten.toLowerCase().replace(" ", "") + "@tlu.edu.vn");


        // Back
        btnBack.setOnClickListener(v -> finish());

        // Danh sách khóa học (demo)
        khoaHocList = new ArrayList<>();
        khoaHocList.add(new KhoaHoc("Phát triển ứng dụng trên thiết bị di động", "Nguyễn Văn Nam", "Hệ thống thông tin", 10));
        khoaHocList.add(new KhoaHoc("Phân tích và thiết kế HTTT", "Nguyễn Văn Nam", "Hệ thống thông tin", 15));
        khoaHocList.add(new KhoaHoc("Phát triển ứng dụng trên thiết bị di động", "Nguyễn Văn Nam", "Hệ thống thông tin", 5));

        adapter = new KhoaHocAdapter(khoaHocList);
        recyclerKhoaHoc.setLayoutManager(new LinearLayoutManager(this));
        recyclerKhoaHoc.setAdapter(adapter);

        ivAvatar.setOnClickListener(v -> {
            Intent intent1 = new Intent(ChiTietGiangVienActivity.this, UserProfileActivity.class);
            startActivity(intent1);
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(ChiTietGiangVienActivity.this, HomeActivity.class); // Assuming student home is default
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish(); // Remove finish() here unless you explicitly want to remove the current activity from stack
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    Intent intent = new Intent(ChiTietGiangVienActivity.this, GroupChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    Intent intent = SessionManager.getCoursesActivityIntent(ChiTietGiangVienActivity.this); // Use helper
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(ChiTietGiangVienActivity.this, UserProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                }
                return false;
            }
        });

    }

    private void setupSessionCheck() {
        sessionHandler = new Handler(Looper.getMainLooper());
        sessionCheckRunnable = () -> {
            SessionManager sessionManager = new SessionManager(ChiTietGiangVienActivity.this);
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(ChiTietGiangVienActivity.this, "Phiên đăng nhập đã hết hạn!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ChiTietGiangVienActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                sessionHandler.postDelayed(sessionCheckRunnable, 10000); // 10 giây kiểm tra lại
            }
        };
        sessionHandler.postDelayed(sessionCheckRunnable, 10000); // bắt đầu sau 10 giây
    }
}
