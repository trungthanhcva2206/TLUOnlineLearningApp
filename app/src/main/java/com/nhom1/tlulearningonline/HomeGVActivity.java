package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeGVActivity extends AppCompatActivity {

    private TextView tvGreeting, tvUserName;
    private EditText edtSearch;
    private ImageView ivNotification, ivAvatar;
    private TextView tvActiveCoursesCount, tvTotalStudents, tvTotalLectures, tvTotalDocuments;

    private RecyclerView recyclerFeaturedTopCoursesGV, recyclerMyCoursesGV;

    private com.nhom1.tlulearningonline.adapters.FeaturedCoursesGVAdapter featuredCoursesGVAdapter;
    private com.nhom1.tlulearningonline.adapters.MyCoursesGVAdapter myCoursesGVAdapter;

    private List<CourseItemGV> featuredCoursesGVList = new ArrayList<>();
    private List<CourseItemGV> myCoursesGVList = new ArrayList<>();

    private BottomNavigationView bottomNavigationView;

    private Handler sessionHandler;
    private Runnable sessionCheckRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_gv);

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
        // fetchUserInfo(); // Uncomment để gọi API lấy thông tin giảng viên
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
                // Triển khai logic tìm kiếm ở đây
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

    private void fetchUserInfo() {
        // Logic gọi API lấy thông tin người dùng giảng viên
        // Tương tự như fetchUserInfo trong HomeActivity của sinh viên
        // Cập nhật tvUserName và ivAvatar
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

}
