package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextWatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeGVActivity extends AppCompatActivity {

    private TextView tvGreeting, tvUserName;
    private EditText edtSearch;
    private ImageView ivNotification, ivAvatar;
    private TextView tvActiveCoursesCount, tvTotalStudents, tvTotalLectures, tvTotalDocuments;

    private RecyclerView recyclerFeaturedTopCoursesGV, recyclerMyCoursesGV;

    private com.nhom1.tlulearningonline.adapters.FeaturedCoursesGVAdapter featuredCoursesGVAdapter;
    private com.nhom1.tlulearningonline.adapters.MyCoursesGVAdapter myCoursesGVAdapter;
    TextView tv_user_name;
    ImageView iv_avatar;

    private List<CourseItemGV> featuredCoursesGVList = new ArrayList<>();
    private List<CourseItemGV> myCoursesGVList = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;

    private Handler sessionHandler;
    private Runnable sessionCheckRunnable;


    private List<CourseItemGV> featuredCoursesGVListFull = new ArrayList<>(); // << Thêm danh sách này

    private List<CourseItemGV> myCoursesGVListFull = new ArrayList<>(); // << Thêm danh sách này


    private void fetchUserInfo() {
        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getUserId();

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy userId!", Toast.LENGTH_SHORT).show();
            sessionManager.clearSession();
            Intent intent = new Intent(HomeGVActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        //
        String url = "http://14.225.207.221:6060/mobile/users/" + userId;

        RequestQueue queue = Volley.newRequestQueue(this);
        Utf8StringRequest request = new Utf8StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject user = new JSONObject(response);
                        String fullName = user.optString("fullname", "Người dùng");
                        String avatarUrl = user.optString("avatar_url", "");

                        tv_user_name.setText(fullName + " 👋");

                        if (avatarUrl != null && !avatarUrl.isEmpty()) {
                            Log.d("AvatarURL", avatarUrl);
                            Glide.with(this)
                                    .load(avatarUrl)
                                    .placeholder(R.drawable.ic_avatar)
                                    .error(R.drawable.ic_avatar)
                                    .circleCrop()
                                    .into(iv_avatar);
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
                        Intent intent = new Intent(HomeGVActivity.this, LoginActivity.class);
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
        setContentView(R.layout.activity_home_gv);
        tv_user_name = findViewById(R.id.tv_user_name);
        iv_avatar = findViewById(R.id.iv_avatar);

        // Ánh xạ View
        tvGreeting = findViewById(R.id.tv_greeting);
        tvUserName = findViewById(R.id.tv_user_name);
        ivNotification = findViewById(R.id.iv_notification);
        ivAvatar = findViewById(R.id.iv_avatar);
        edtSearch = findViewById(R.id.edt_search);

        tvActiveCoursesCount = findViewById(R.id.tv_active_courses_count);
        tvTotalStudents = findViewById(R.id.tv_total_students);
        tvTotalLectures = findViewById(R.id.tv_total_lectures);
        tvTotalDocuments = findViewById(R.id.tv_total_documents);

        recyclerFeaturedTopCoursesGV = findViewById(R.id.recycler_featured_top_courses_gv);
        recyclerMyCoursesGV = findViewById(R.id.recycler_my_courses_gv);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Setup dữ liệu mẫu
        setupDummyData();
        fetchUserInfo();
        fetchCourses();// Uncomment để gọi API lấy thông tin giảng viên
        setupSessionCheck();

        // Cập nhật thống kê
        updateStatistics();

        // Setup RecyclerView cho Khoá học nổi bật của tôi
        featuredCoursesGVAdapter = new com.nhom1.tlulearningonline.adapters.FeaturedCoursesGVAdapter(featuredCoursesGVList);
        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerFeaturedTopCoursesGV.setLayoutManager(featuredLayoutManager);
        recyclerFeaturedTopCoursesGV.setAdapter(featuredCoursesGVAdapter);

        // Setup RecyclerView cho Khoá học của tôi
        myCoursesGVAdapter = new com.nhom1.tlulearningonline.adapters.MyCoursesGVAdapter(myCoursesGVList);
        LinearLayoutManager myCoursesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerMyCoursesGV.setLayoutManager(myCoursesLayoutManager);
        recyclerMyCoursesGV.setAdapter(myCoursesGVAdapter);

        // Xử lý sự kiện click
        ivNotification.setOnClickListener(v -> Toast.makeText(HomeGVActivity.this, "Thông báo!", Toast.LENGTH_SHORT).show());
        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeGVActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Lọc cả hai danh sách
                filterFeaturedCoursesGV(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    // Already on HomeGV, do nothing
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    Intent intent = new Intent(HomeGVActivity.this, GroupChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    Intent intent = SessionManager.getCoursesActivityIntent(HomeGVActivity.this); // Use helper
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Intent intent = new Intent(HomeGVActivity.this, UserProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void updateStatistics() {
        // Cập nhật các TextView thống kê từ dữ liệu hoặc API
        tvActiveCoursesCount.setText(String.valueOf(3)); // Lấy từ dữ liệu thực tế
        tvTotalStudents.setText(String.valueOf(200));   // Lấy từ dữ liệu thực tế
        tvTotalLectures.setText(String.valueOf(60));    // Lấy từ dữ liệu thực tế
        tvTotalDocuments.setText(String.valueOf(70));   // Lấy từ dữ liệu thực tế
    }


    private void setupSessionCheck() {
        sessionHandler = new Handler(Looper.getMainLooper());
        sessionCheckRunnable = () -> {
            SessionManager sessionManager = new SessionManager(HomeGVActivity.this);
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(HomeGVActivity.this, "Phiên đăng nhập đã hết hạn!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeGVActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                sessionHandler.postDelayed(sessionCheckRunnable, 10000); // 10 giây kiểm tra lại
            }
        };
        sessionHandler.postDelayed(sessionCheckRunnable, 10000); // bắt đầu sau 10 giây
    }

    /**
     * Tạo dữ liệu mẫu cho các danh sách khóa học nổi bật và khóa học của tôi.
     */
    private void setupDummyData() {
        featuredCoursesGVList.clear();
        myCoursesGVList.clear();

        featuredCoursesGVList.add(new CourseItemGV("Thiết kế và phát triển game",
                "Môn Thiết kế và Phát triển Game sẽ hướng dẫn bạn từ ý tưởng đến sản phẩm game...", 15));
        featuredCoursesGVList.add(new CourseItemGV("Trí tuệ nhân tạo nâng cao",
                "Tiếp cận các thuật toán AI phức tạp và ứng dụng thực tế.", 25));

        myCoursesGVList.add(new CourseItemGV("Thuật toán ứng dụng",
                "Môn Thuật toán Ứng dụng trang bị cho bạn kiến thức và kỹ năng xây dựng giải pháp hiệu quả cho các bài toán thực tế.", 15));
        myCoursesGVList.add(new CourseItemGV("Công nghệ đa phương tiện",
                "Chuyên đề về công nghệ đa phương tiện là cơ hội để bạn cập nhật những lĩnh vực công nghệ thông tin mới nhất.", 20));
        myCoursesGVList.add(new CourseItemGV("Hệ thống thông tin quản lý",
                "Tìm hiểu về cách xây dựng và quản lý các hệ thống thông tin trong tổ chức.", 18));
    }

    // --- THÊM CÁC PHƯƠNG THỨC LỌC ---
    private void filterFeaturedCoursesGV(String text) {
        List<CourseItemGV> filteredList = new ArrayList<>();
        for (CourseItemGV item : featuredCoursesGVListFull) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        featuredCoursesGVAdapter.filterList(filteredList);
    }


    private void fetchCourses() {
        SessionManager sm = new SessionManager(this);
        String userId = sm.getUserId();
        if (userId == null) return; // hoặc xử lý logout

        RequestQueue queue = Volley.newRequestQueue(this);

        // 1. Lấy tất cả khóa học
        String urlCourses = "http://14.225.207.221:6060/mobile/courses";
        StringRequest reqCourses = new Utf8StringRequest(Request.Method.GET, urlCourses,
                coursesResponse -> {
                    try {
                        JSONArray coursesArr = new JSONArray(coursesResponse);

                        // 2. Gọi tiếp API để lấy bài giảng
                        String urlLessons = "http://14.225.207.221:6060/mobile/lessons";
                        StringRequest reqLessons = new Utf8StringRequest(Request.Method.GET, urlLessons,
                                lessonsResponse -> {
                                    try {
                                        JSONArray lessonsArr = new JSONArray(lessonsResponse);

                                        // Đếm số bài giảng theo courseId
                                        Map<String,Integer> lessonCountMap = new HashMap<>();
                                        for (int i = 0; i < lessonsArr.length(); i++) {
                                            JSONObject l = lessonsArr.getJSONObject(i);
                                            String cId = l.optString("courseId");
                                            lessonCountMap.put(cId, lessonCountMap.getOrDefault(cId, 0) + 1);
                                        }

                                        // Xoá danh sách cũ
                                        featuredCoursesGVList.clear();
                                        myCoursesGVList.clear();
                                        featuredCoursesGVListFull.clear(); // << Xóa
                                        myCoursesGVListFull.clear();

                                        // 3. Lặp qua tất cả các khóa học
                                        for (int i = 0; i < coursesArr.length(); i++) {
                                            JSONObject c = coursesArr.getJSONObject(i);

                                            String id     = c.getString("id");
                                            String title  = c.getString("title");
                                            String desc   = c.getString("description");
                                            String tId    = c.getString("teacherId");
                                            int count     = lessonCountMap.getOrDefault(id, 0);

                                            CourseItemGV item = new CourseItemGV(title, desc, count);

                                            // ✅ Thêm vào danh sách featured (tất cả các khóa học)
                                            featuredCoursesGVList.add(item);
                                            featuredCoursesGVListFull.add(item);

                                            // ✅ Nếu là của giảng viên hiện tại thì thêm vào danh sách của tôi
                                            if (userId.equals(tId)) {
                                                myCoursesGVList.add(item);
                                                myCoursesGVListFull.add(item);
                                            }
                                        }

                                        // Thông báo adapter cập nhật UI
                                        featuredCoursesGVAdapter.notifyDataSetChanged();
                                        myCoursesGVAdapter.notifyDataSetChanged();

                                        // Cập nhật số liệu thống kê
                                        tvActiveCoursesCount.setText(String.valueOf(myCoursesGVList.size()));
                                        tvTotalLectures.setText(String.valueOf(lessonCountMap.size()));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                },
                                error -> Toast.makeText(this, "Không tải được danh sách bài học", Toast.LENGTH_SHORT).show()
                        );

                        queue.add(reqLessons);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(this, "Không tải được danh sách khóa học", Toast.LENGTH_SHORT).show()
        );

        queue.add(reqCourses);
    }



}
