package com.nhom1.tlulearningonline;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private TextView tvGreeting, tvUserName;
    private EditText edtSearch;
    private ImageView ivNotification, ivAvatar;
    private ImageView ivSeeMoreCourses, ivSeeMoreLecturers;
    private RecyclerView recyclerFeaturedCourses, recyclerInProgressCourses, recyclerTeachers;
    private TopCourseAdapter featuredCoursesAdapter;
//    private FeaturedCoursesAdapter inProgressCoursesAdapter;
    private TeachersAdapter teachersAdapter;
    private List<CourseItem> featuredCoursesList = new ArrayList<>();
//    private List<CourseItem> inProgressCoursesList = new ArrayList<>();
    private List<TeacherItem> teacherList = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;

    private Handler sessionHandler;
    private Runnable sessionCheckRunnable;

    // Thêm tham chiếu cho LinearLayout chứa các filter chips
    private LinearLayout horizontalDepartmentChipsContainer;

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
        Utf8StringRequest request = new Utf8StringRequest(Request.Method.GET, url,
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
//        recyclerInProgressCourses = findViewById(R.id.inprogress_course);
        recyclerTeachers = findViewById(R.id.recycler_lecturers);

        ivSeeMoreLecturers = findViewById(R.id.iv_see_more_lecturers);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Ánh xạ LinearLayout chứa các filter chips
        horizontalDepartmentChipsContainer = findViewById(R.id.horizontal_chip_container);


        setupDummyData();
        fetchLessonsAndCourses();
        fetchUserInfo();
        setupSessionCheck();

        featuredCoursesAdapter = new TopCourseAdapter(featuredCoursesList);
        LinearLayoutManager featuredCoursesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerFeaturedCourses.setLayoutManager(featuredCoursesLayoutManager);
        recyclerFeaturedCourses.setAdapter(featuredCoursesAdapter);

//        inProgressCoursesAdapter = new FeaturedCoursesAdapter(inProgressCoursesList);
//        LinearLayoutManager inProgressCoursesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerInProgressCourses.setLayoutManager(inProgressCoursesLayoutManager);
//        recyclerInProgressCourses.setAdapter(inProgressCoursesAdapter);

        teachersAdapter = new TeachersAdapter(teacherList);
        LinearLayoutManager teachersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerTeachers.setLayoutManager(teachersLayoutManager);
        recyclerTeachers.setAdapter(teachersAdapter);

        ivNotification.setOnClickListener(v -> Toast.makeText(HomeActivity.this, "Thông báo!", Toast.LENGTH_SHORT).show());
        ivAvatar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });

        // Xử lý khi nhấn vào thanh tìm kiếm (chuyển hướng ngay lập tức)
        edtSearch.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, XemKhoaHocActivity.class);
            startActivity(intent);
        });

        // Xử lý khi nhấn vào từng chip bộ môn
        if (horizontalDepartmentChipsContainer != null) {
            for (int i = 0; i < horizontalDepartmentChipsContainer.getChildCount(); i++) {
                View child = horizontalDepartmentChipsContainer.getChildAt(i);
                if (child instanceof TextView) {
                    TextView departmentChip = (TextView) child;
                    departmentChip.setOnClickListener(v -> {
                        String departmentName = departmentChip.getText().toString();
                        // Chuyển hướng sang XemKhoaHocActivity và truyền tên bộ môn
                        Intent intent = new Intent(HomeActivity.this, XemKhoaHocActivity.class);
                        intent.putExtra("search_query", departmentName); // Sử dụng cùng key với search bar
                        startActivity(intent);
                    });
                }
            }
        }


        ivSeeMoreLecturers.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, DanhSachGiangVienActivity.class);
            startActivity(intent);
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
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void performSearch() {
        // Phương thức này hiện tại không còn được gọi trực tiếp từ UI trong HomeActivity
        // Nó có thể được xóa hoặc giữ lại nếu bạn có ý định dùng nó ở nơi khác.
        String searchQuery = edtSearch.getText().toString().trim();
        if (!searchQuery.isEmpty()) {
            Intent searchIntent = new Intent(HomeActivity.this, XemKhoaHocActivity.class);
            searchIntent.putExtra("search_query", searchQuery);
            startActivity(searchIntent);
        } else {
            Toast.makeText(HomeActivity.this, "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
        }
    }

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
                sessionHandler.postDelayed(sessionCheckRunnable, 10000);
            }
        };
        sessionHandler.postDelayed(sessionCheckRunnable, 10000);
    }

    private void setupDummyData() {
        featuredCoursesList.clear();
//        inProgressCoursesList.clear();
        teacherList.clear();

        teacherList.add(new TeacherItem("Nguyễn Văn Nam", R.drawable.gv_ng_van_nam_portrait));
        teacherList.add(new TeacherItem("Nguyễn Tu Trung", R.drawable.gv_ng_tu_trung_portrait));
        teacherList.add(new TeacherItem("Nguyễn Thị Thu Hương", R.drawable.gv_ng_thi_thu_huong));
        teacherList.add(new TeacherItem("Trương Xuân Nam", R.drawable.gv_truong_xuan_nam));
    }
    private void fetchLessonsAndCourses() {
        String lessonUrl = "http://14.225.207.221:6060/mobile/lessons";
        String courseUrl = "http://14.225.207.221:6060/mobile/courses";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest lessonRequest = new JsonArrayRequest(Request.Method.GET, lessonUrl, null,
                lessonResponse -> {
                    Map<String, Integer> lessonCountMap = new HashMap<>();

                    for (int i = 0; i < lessonResponse.length(); i++) {
                        try {
                            JSONObject lesson = lessonResponse.getJSONObject(i);
                            String courseId = lesson.getString("courseId");
                            lessonCountMap.put(courseId, lessonCountMap.getOrDefault(courseId, 0) + 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    JsonArrayRequest courseRequest = new JsonArrayRequest(Request.Method.GET, courseUrl, null,
                            courseResponse -> {
                                featuredCoursesList.clear();
                                for (int i = 0; i < courseResponse.length(); i++) {
                                    try {
                                        JSONObject course = courseResponse.getJSONObject(i);
                                        String id = course.getString("id");
                                        String title = course.getString("title");
                                        String teacherName = course.optString("teacherName", "Chưa rõ");
                                        String departmentName = course.optString("departmentName", "Chưa rõ");
                                        String des = course.optString("description", "Chưa rõ");

                                        int soBaiHoc = lessonCountMap.getOrDefault(id, 0);

                                        CourseItem item = new CourseItem(id, title, teacherName, departmentName,des, soBaiHoc);
                                        featuredCoursesList.add(item);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                featuredCoursesAdapter.notifyDataSetChanged();
                            },
                            error -> {
                                Toast.makeText(this, "Lỗi lấy danh sách khoá học!", Toast.LENGTH_SHORT).show();
                                error.printStackTrace();
                            }
                    );

                    queue.add(courseRequest);
                },
                error -> {
                    Toast.makeText(this, "Lỗi lấy danh sách bài học!", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
        );

        queue.add(lessonRequest);
    }

}