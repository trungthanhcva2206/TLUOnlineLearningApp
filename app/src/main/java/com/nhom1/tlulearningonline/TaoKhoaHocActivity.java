package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TaoKhoaHocActivity extends AppCompatActivity {

    private TextInputLayout layoutTenKhoaHoc, layoutMoTaKhoaHoc;
    private TextView edtTenKhoaHoc, edtMoTaKhoaHoc;
    private Button btnThemBaiHoc, btnTaoKhoaHoc;
    private LinearLayout layoutDanhSachBaiHoc;

    private BottomNavigationView bottomNavigationView;

    private final List<BaiHoc> danhSachBaiHoc = new ArrayList<>();
    private static final int REQUEST_THEM_BAI_HOC = 1;

    private Spinner spinnerBoMon;
    private List<Department> departments = new ArrayList<>();
    private ArrayAdapter<Department> departmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tao_khoa_hoc);

        edtTenKhoaHoc = findViewById(R.id.edt_ten_khoa_hoc);
        edtMoTaKhoaHoc = findViewById(R.id.edt_mo_ta_khoa_hoc);
        layoutTenKhoaHoc = findViewById(R.id.layout_ten_khoa_hoc);
        layoutMoTaKhoaHoc = findViewById(R.id.layout_mo_ta_khoa_hoc);
        btnTaoKhoaHoc = findViewById(R.id.btn_tao_khoa_hoc);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        spinnerBoMon = findViewById(R.id.spinner_bo_mon);

        ImageView btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());


        btnTaoKhoaHoc.setOnClickListener(v -> {
            String tenKhoaHoc = edtTenKhoaHoc.getText().toString().trim();
            String moTaKhoaHoc = edtMoTaKhoaHoc.getText().toString().trim();

            Department selectedDepartment = (Department) spinnerBoMon.getSelectedItem();
            String departmentId = selectedDepartment.getId();

            boolean isValid = true;
            layoutTenKhoaHoc.setError(null);
            layoutMoTaKhoaHoc.setError(null);

            if (TextUtils.isEmpty(tenKhoaHoc)) {
                layoutTenKhoaHoc.setError("Vui lòng nhập tên khóa học");
                isValid = false;
            }
            if (TextUtils.isEmpty(moTaKhoaHoc)) {
                layoutMoTaKhoaHoc.setError("Vui lòng nhập mô tả khóa học");
                isValid = false;
            }

            if (isValid) {
                // Gọi hàm tạo khóa học qua API
                taoKhoaHoc(tenKhoaHoc, moTaKhoaHoc, departmentId);
            }
        });


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    Intent intent = new Intent(TaoKhoaHocActivity.this, HomeGVActivity.class); // Assuming student home is default
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish(); // Remove finish() here unless you explicitly want to remove the current activity from stack
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    Intent intent = new Intent(TaoKhoaHocActivity.this, GroupChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    Intent intent = SessionManager.getCoursesActivityIntent(TaoKhoaHocActivity.this); // Use helper
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(TaoKhoaHocActivity.this, UserProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    // finish();
                    return true;
                }
                return false;
            }
        });



        spinnerBoMon = findViewById(R.id.spinner_bo_mon);

        departmentAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                departments
        );
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBoMon.setAdapter(departmentAdapter);

        loadDepartmentsFromAPI();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_THEM_BAI_HOC && resultCode == RESULT_OK && data != null) {
            String tenBaiHoc = data.getStringExtra("tenBaiHoc");
            String videoUri = data.getStringExtra("videoUri");
            String taiLieu = data.getStringExtra("tenTaiLieu");

            BaiHoc baiHoc = new BaiHoc(tenBaiHoc, videoUri, taiLieu);
            danhSachBaiHoc.add(baiHoc);

            View view = LayoutInflater.from(this).inflate(R.layout.item_bai_hoc_gv, layoutDanhSachBaiHoc, false);
            TextView tvTenBaiHoc = view.findViewById(R.id.tvTenBaiHoc);
            ImageView btnXoaBaiHoc = view.findViewById(R.id.btnXoaBaiHoc);

            tvTenBaiHoc.setText("- " + tenBaiHoc);

            btnXoaBaiHoc.setOnClickListener(v -> {
                layoutDanhSachBaiHoc.removeView(view);
                danhSachBaiHoc.remove(baiHoc);
            });

            layoutDanhSachBaiHoc.addView(view);
        }
    }

    // Class tạm cho bài học
    private static class BaiHoc {
        String ten;
        String videoUri;
        String taiLieu;

        public BaiHoc(String ten, String videoUri, String taiLieu) {
            this.ten = ten;
            this.videoUri = videoUri;
            this.taiLieu = taiLieu;
        }
    }

    private void loadDepartmentsFromAPI() {
        String url = "http://14.225.207.221:6060/mobile/departments";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    departments.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            String id = obj.getString("id");
                            String name = obj.getString("name");
                            departments.add(new Department(id, name));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    departmentAdapter.notifyDataSetChanged();
                },
                error -> {
                    Toast.makeText(this, "Lỗi tải danh sách bộ môn", Toast.LENGTH_SHORT).show();
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void taoKhoaHoc(String tenKhoaHoc, String moTaKhoaHoc, String departmentId) {
        String url = "http://14.225.207.221:6060/mobile/courses";

        SessionManager sessionManager = new SessionManager(this);
        String teacherId = sessionManager.getUserId();

        JSONObject body = new JSONObject();
        try {
            body.put("title", tenKhoaHoc);
            body.put("description", moTaKhoaHoc);
            body.put("departmentId", departmentId);
            body.put("teacherId", teacherId);
            body.put("status", "ACTIVE");
            // Không gửi thumbnailUrl nếu không cần
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi tạo JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        com.android.volley.toolbox.JsonObjectRequest request = new com.android.volley.toolbox.JsonObjectRequest(
                Request.Method.POST,
                url,
                body,
                response -> {
                    Toast.makeText(this, "Tạo khóa học thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay về sau khi tạo xong
                },
                error -> {
                    Toast.makeText(this, "Tạo khóa học thất bại!", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }


}
