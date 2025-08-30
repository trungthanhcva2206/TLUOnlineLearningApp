package com.nhom1.tlulearningonline;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DanhSachGiangVienActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private GiangVienAdapter adapter;
    private List<GiangVien> danhSachGiangVien;
    private List<GiangVien> danhSachGiangVienDayDu; // Thêm danh sách này để giữ bản gốc

    private ImageView ivAvatar;
    private EditText edtSearch; // Thêm biến cho EditText

    private BottomNavigationView bottomNavigationView;
    private Handler sessionHandler;
    private Runnable sessionCheckRunnable;

    private void fetchUserInfo() {
        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getUserId();

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy userId!", Toast.LENGTH_SHORT).show();
            sessionManager.clearSession();
            Intent intent = new Intent(DanhSachGiangVienActivity.this, LoginActivity.class);
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
                        Intent intent = new Intent(DanhSachGiangVienActivity.this, LoginActivity.class);
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

    private void fetchDanhSachGiangVien() {
        String url = "http://14.225.207.221:6060/mobile/users/role?role=TEACHER";

        Utf8StringRequest request = new Utf8StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        danhSachGiangVien.clear();
                        danhSachGiangVienDayDu.clear(); // Xóa cả danh sách đầy đủ

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            GiangVien gv = new GiangVien(
                                    obj.optString("id"),
                                    obj.optString("username"),
                                    obj.optString("password"),
                                    obj.optString("role"),
                                    obj.optString("avatar_url"),
                                    obj.optString("fullname"),
                                    obj.optString("status"),
                                    obj.optString("createdAt"),
                                    obj.optString("updatedAt")
                            );
                            danhSachGiangVien.add(gv);
                            danhSachGiangVienDayDu.add(gv); // Thêm vào danh sách đầy đủ
                        }

                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Lỗi xử lý dữ liệu giảng viên", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Không thể kết nối máy chủ", Toast.LENGTH_SHORT).show();
                });


        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_giang_vien);

        ImageView ivBack = findViewById(R.id.iv_back);
        ivBack.setOnClickListener(v -> finish());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ivAvatar = findViewById(R.id.iv_avatar);
        edtSearch = findViewById(R.id.edt_search); // Ánh xạ EditText


        recyclerView = findViewById(R.id.recyclerGiangVien);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fetchUserInfo();
        setupSessionCheck();
        danhSachGiangVien = new ArrayList<>();
        danhSachGiangVienDayDu = new ArrayList<>(); // Khởi tạo


        adapter = new GiangVienAdapter(danhSachGiangVien, giangVien -> {
            Intent intent = new Intent(this, ChiTietGiangVienActivity.class);
            intent.putExtra("ten", giangVien.getFullname());
            intent.putExtra("avatarResId", giangVien.getAvatarUrl());
            startActivity(intent);
        });


        recyclerView.setAdapter(adapter);
        fetchDanhSachGiangVien();

        // --- THÊM LISTENER CHO TÌM KIẾM ---
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSachGiangVienActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(DanhSachGiangVienActivity.this, HomeActivity.class); // Assuming student home is default
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish(); // Remove finish() here unless you explicitly want to remove the current activity from stack
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    Intent intent = new Intent(DanhSachGiangVienActivity.this, GroupChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    Intent intent = SessionManager.getCoursesActivityIntent(DanhSachGiangVienActivity.this); // Use helper
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(DanhSachGiangVienActivity.this, UserProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                }
                return false;
            }
        });

    }

    // --- THÊM PHƯƠNG THỨC LỌC DANH SÁCH ---
    private void filter(String text) {
        ArrayList<GiangVien> filteredList = new ArrayList<>();
        for (GiangVien item : danhSachGiangVienDayDu) {
            // Lọc theo tên giảng viên (không phân biệt chữ hoa, chữ thường)
            if (item.getFullname().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        // Cập nhật danh sách hiển thị cho adapter
        adapter.filterList(filteredList);
    }


    private void setupSessionCheck() {
        sessionHandler = new Handler(Looper.getMainLooper());
        sessionCheckRunnable = () -> {
            SessionManager sessionManager = new SessionManager(DanhSachGiangVienActivity.this);
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(DanhSachGiangVienActivity.this, "Phiên đăng nhập đã hết hạn!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DanhSachGiangVienActivity.this, LoginActivity.class);
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