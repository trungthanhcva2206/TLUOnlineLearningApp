package com.nhom1.tlulearningonline;

import static androidx.core.content.ContextCompat.startActivity;

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
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView tvGreeting, tvUserName;
    private EditText edtSearch;
    private ImageView ivNotification, ivAvatar;
    private ImageView ivSeeMoreCourses, ivSeeMoreLecturers;

    private RecyclerView recyclerFeaturedCourses, recyclerInProgressCourses, recyclerTeachers;

    private FeaturedCoursesAdapter featuredCoursesAdapter;
    private FeaturedCoursesAdapter inProgressCoursesAdapter;
    private TeachersAdapter teachersAdapter;
    private List<CourseItem> featuredCoursesList;
    private List<CourseItem> inProgressCoursesList;
    private List<TeacherItem> teacherList;

    private BottomNavigationView bottomNavigationView;

    private Handler sessionHandler;
    private Runnable sessionCheckRunnable;

    private void fetchUserInfo() {
        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getUserId();

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(this, "Không tìm thấy userId!", Toast.LENGTH_SHORT).show();
            sessionManager.clearSession();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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

                        tvUserName.setText(fullName + " 👋");

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
                        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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
        setContentView(R.layout.activity_home_sv);

        tvGreeting = findViewById(R.id.tv_greeting);
        tvUserName = findViewById(R.id.tv_user_name);
        ivNotification = findViewById(R.id.iv_notification);
        ivAvatar = findViewById(R.id.iv_avatar);
        edtSearch = findViewById(R.id.edt_search);

        recyclerFeaturedCourses = findViewById(R.id.recycler_featured_courses);
        recyclerInProgressCourses = findViewById(R.id.inprogress_course);
        recyclerTeachers = findViewById(R.id.recycler_lecturers);

        ivSeeMoreLecturers = findViewById(R.id.iv_see_more_lecturers);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupDummyData();
        fetchUserInfo();
        setupSessionCheck();

        featuredCoursesAdapter = new FeaturedCoursesAdapter(featuredCoursesList);
        LinearLayoutManager featuredCoursesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerFeaturedCourses.setLayoutManager(featuredCoursesLayoutManager);
        recyclerFeaturedCourses.setAdapter(featuredCoursesAdapter);

        inProgressCoursesAdapter = new FeaturedCoursesAdapter(inProgressCoursesList);
        LinearLayoutManager inProgressCoursesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerInProgressCourses.setLayoutManager(inProgressCoursesLayoutManager);
        recyclerInProgressCourses.setAdapter(inProgressCoursesAdapter);

        teachersAdapter = new TeachersAdapter(teacherList);
        LinearLayoutManager teachersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerTeachers.setLayoutManager(teachersLayoutManager);
        recyclerTeachers.setAdapter(teachersAdapter);

        ivNotification.setOnClickListener(v -> Toast.makeText(HomeActivity.this, "Thông báo!", Toast.LENGTH_SHORT).show());
        ivAvatar.setOnClickListener(v -> Toast.makeText(HomeActivity.this, "Mở Hồ sơ cá nhân!", Toast.LENGTH_SHORT).show());

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Bạn có thể triển khai logic lọc tìm kiếm ở đây.
                // Hiện tại, chỉ là một Toast.
                // Toast.makeText(HomeActivity.this, "Tìm kiếm: " + s.toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        ivSeeMoreLecturers.setOnClickListener(v -> { // "Xem thêm giảng viên"
            Toast.makeText(HomeActivity.this, "Xem thêm giảng viên", Toast.LENGTH_SHORT).show();
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    startActivity(new Intent(HomeActivity.this, GroupChatActivity.class));
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    startActivity(new Intent(HomeActivity.this, XemKhoaHocActivity.class));
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(HomeActivity.this, UserProfileActivity.class));
                    // finish();
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    /**
     * Tạo dữ liệu mẫu cho các danh sách khóa học nổi bật, khóa học đang học và giảng viên.
     */
    private void setupSessionCheck() {
        sessionHandler = new Handler(Looper.getMainLooper());
        sessionCheckRunnable = () -> {
            SessionManager sessionManager = new SessionManager(HomeActivity.this);
            if (!sessionManager.isLoggedIn()) {
                Toast.makeText(HomeActivity.this, "Phiên đăng nhập đã hết hạn!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                sessionHandler.postDelayed(sessionCheckRunnable, 10000); // 10 giây kiểm tra lại
            }
        };
        sessionHandler.postDelayed(sessionCheckRunnable, 10000); // bắt đầu sau 10 giây
    }
    private void setupDummyData() {
        // Dữ liệu mẫu cho Khóa học nổi bật
        featuredCoursesList = new ArrayList<>();
        featuredCoursesList.add(new CourseItem("Phát triển ứng dụng trên thiết bị di động", "Nguyễn Văn Nam", "Hệ thống thông tin", 80, false));
        featuredCoursesList.add(new CourseItem("Phân tích thiết kế hệ thống thông tin", "Trần Hùng Anh", "Hệ thống thông tin", 60, true));
        featuredCoursesList.add(new CourseItem("Cơ sở dữ liệu nâng cao", "Phạm Nhật Anh", "Công nghệ phần mềm", 95, false));
        featuredCoursesList.add(new CourseItem("Lập trình Web Frontend", "Lê Văn B", "Khoa học máy tính", 40, true));
        featuredCoursesList.add(new CourseItem("Trí tuệ nhân tạo cơ bản", "Đinh Thị C", "Công nghệ thông tin", 70, false));

        // Dữ liệu mẫu cho Khóa học đang học (chỉ một khóa học duy nhất)
        inProgressCoursesList = new ArrayList<>();
        inProgressCoursesList.add(new CourseItem("Tương tác người máy", "Nguyễn Thị Thu Hương", "Khoa CNTT", 10, true)); // Chỉ một khóa học duy nhất

        // Dữ liệu mẫu cho Giảng viên
        teacherList = new ArrayList<>();
        teacherList.add(new TeacherItem("Nguyễn Văn Nam", R.drawable.ic_avatar));
        teacherList.add(new TeacherItem("Nguyễn Tu Trung", R.drawable.ic_avatar));
        teacherList.add(new TeacherItem("Nguyễn Thị Thu Hương", R.drawable.ic_avatar));
        teacherList.add(new TeacherItem("Trương Xuân Nam", R.drawable.ic_avatar));
        teacherList.add(new TeacherItem("Trần Hồng Diệp", R.drawable.ic_avatar));
    }
}
