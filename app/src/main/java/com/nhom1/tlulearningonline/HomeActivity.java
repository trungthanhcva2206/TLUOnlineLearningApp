package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private TextView tvGreeting, tvUserName;
    private EditText edtSearch;
    private ImageView ivNotification, ivAvatar, ivSeeMoreCourses, ivSeeMoreLecturers;
    private RecyclerView recyclerFeaturedCourses, recyclerLecturers;
    private BottomNavigationView bottomNavigationView;

    private FeaturedCoursesAdapter featuredCoursesAdapter;
    private TeachersAdapter teachersAdapter;
    private List<CourseItem> featuredCoursesList;
    private List<TeacherItem> lecturerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_dashboard);

        tvGreeting = findViewById(R.id.tv_greeting);
        tvUserName = findViewById(R.id.tv_user_name);
        ivNotification = findViewById(R.id.iv_notification);
        ivAvatar = findViewById(R.id.iv_avatar);
        edtSearch = findViewById(R.id.edt_search);

        recyclerFeaturedCourses = findViewById(R.id.recycler_featured_courses);
        recyclerLecturers = findViewById(R.id.recycler_lecturers);
        ivSeeMoreCourses = findViewById(R.id.iv_see_more_courses);
        ivSeeMoreLecturers = findViewById(R.id.iv_see_more_lecturers);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupDummyData();

        featuredCoursesAdapter = new FeaturedCoursesAdapter(featuredCoursesList);
        LinearLayoutManager featuredCoursesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerFeaturedCourses.setLayoutManager(featuredCoursesLayoutManager);
        recyclerFeaturedCourses.setAdapter(featuredCoursesAdapter);

        teachersAdapter = new TeachersAdapter(lecturerList);
        LinearLayoutManager lecturersLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerLecturers.setLayoutManager(lecturersLayoutManager);
        recyclerLecturers.setAdapter(teachersAdapter);

        ivNotification.setOnClickListener(v -> Toast.makeText(HomeActivity.this, "Thông báo!", Toast.LENGTH_SHORT).show());
        ivAvatar.setOnClickListener(v -> Toast.makeText(HomeActivity.this, "Mở Hồ sơ cá nhân!", Toast.LENGTH_SHORT).show());

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Bạn có thể triển khai logic lọc tìm kiếm ở đây.
                // Hiện tại, chỉ là một Toast.
                // Toast.makeText(MainActivity.this, "Tìm kiếm: " + s.toString(), Toast.LENGTH_SHORT).show();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        ivSeeMoreCourses.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Xem thêm khoá học", Toast.LENGTH_SHORT).show();
            // Chuyển hướng đến một activity danh sách khóa học riêng
            Intent intent = new Intent(HomeActivity.this, XemKhoaHocActivity.class); // Giả sử XemKhoaHocActivity là danh sách khóa học của bạn
            startActivity(intent);
        });

        ivSeeMoreLecturers.setOnClickListener(v -> {
            Toast.makeText(HomeActivity.this, "Xem thêm giảng viên", Toast.LENGTH_SHORT).show();
            // Chuyển hướng đến một activity danh sách giảng viên riêng
        });


        // --- XỬ LÝ THANH ĐIỀU HƯỚNG DƯỚI CÙNG ---
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    // Đã ở Trang chủ, không làm gì hoặc tải lại nếu cần
                    return true;
                } else if (itemId == R.id.nav_forum) {
                    startActivity(new Intent(HomeActivity.this, GroupChatActivity.class)); // Chuyển đến activity Chat/Diễn đàn
                    return true;
                } else if (itemId == R.id.nav_courses) {
                    startActivity(new Intent(HomeActivity.this, XemKhoaHocActivity.class)); // Chuyển đến activity Danh sách khóa học
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    Toast.makeText(HomeActivity.this, "Chức năng Hồ sơ", Toast.LENGTH_SHORT).show();
                    // Chuyển đến activity Hồ sơ
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    /**
     * Tạo dữ liệu mẫu cho các danh sách khóa học nổi bật và giảng viên.
     */
    private void setupDummyData() {
        // Dữ liệu mẫu cho Khóa học nổi bật
        featuredCoursesList = new ArrayList<>();
        featuredCoursesList.add(new CourseItem("Phát triển ứng dụng trên thiết bị di động", "Nguyễn Văn Nam", "Hệ thống thông tin", 80, false));
        featuredCoursesList.add(new CourseItem("Phân tích thiết kế hệ thống thông tin", "Trần Hùng Anh", "Hệ thống thông tin", 60, true));
        featuredCoursesList.add(new CourseItem("Cơ sở dữ liệu nâng cao", "Phạm Nhật Anh", "Công nghệ phần mềm", 95, false));
        featuredCoursesList.add(new CourseItem("Lập trình Web Frontend", "Lê Văn B", "Khoa học máy tính", 40, true));
        featuredCoursesList.add(new CourseItem("Trí tuệ nhân tạo cơ bản", "Đinh Thị C", "Công nghệ thông tin", 70, false));

        // Dữ liệu mẫu cho Giảng viên (sử dụng ID tài nguyên avatar mẫu)
        lecturerList = new ArrayList<>();
        // Bạn có thể thêm các drawable placeholder như R.drawable.lecturer_avatar_1, 2, 3...
        // Hoặc sử dụng ic_avatar trực tiếp nếu đó là một icon người dùng chung
        lecturerList.add(new TeacherItem("Nguyễn Văn Nam", R.drawable.ic_avatar));
        lecturerList.add(new TeacherItem("Nguyễn Tú Trung", R.drawable.ic_avatar));
        lecturerList.add(new TeacherItem("Nguyễn Thị Thu Hương", R.drawable.ic_avatar));
        lecturerList.add(new TeacherItem("Trương Xuân Nam", R.drawable.ic_avatar));
        lecturerList.add(new TeacherItem("Phạm Nhật Anh", R.drawable.ic_avatar));
    }
}
