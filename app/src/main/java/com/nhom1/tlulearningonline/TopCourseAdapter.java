package com.nhom1.tlulearningonline;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopCourseAdapter extends RecyclerView.Adapter<TopCourseAdapter.CourseViewHolder> {

    private List<CourseItem> courseList;

    public TopCourseAdapter(List<CourseItem> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_khoa_hoc_sv, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseItem course = courseList.get(position);

        holder.tvCourseName.setText(course.getTitle());
        holder.tvInstructor.setText("GV: " + course.getInstructor());
        holder.tvDepartment.setText("Bộ môn: " + course.getDepartment());
        holder.btnLessonCount.setText(course.getSoBaiHoc() + " bài học");

        if (course.isRegistered()) {
            holder.ivFavorite.setImageResource(R.drawable.ic_star_filled);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.ic_star_border);
        }
        // Đổi màu nền xen kẽ
        int colorId = (position % 2 == 0) ? R.color.blue_3 : R.color.blue;
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), colorId));

        // Bấm vào thẻ để xem chi tiết
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ChiTietKhoaHocActivity.class);
            intent.putExtra("course_id", course.getId());
            intent.putExtra("tieu_de", course.getTitle());
            intent.putExtra("tac_gia", course.getInstructor());
            intent.putExtra("mo_ta", course.getDepartment());
            intent.putExtra("des", course.getDes());

            intent.putExtra("so_bai", course.getSoBaiHoc());
            v.getContext().startActivity(intent);
        });
        holder.ivFavorite.setOnClickListener(v -> {
            Context context = v.getContext();
            if (!course.isRegistered()) {
                SessionManager sessionManager = new SessionManager(context);
                String userId = sessionManager.getUserId();

                if (userId == null || userId.isEmpty()) {
                    Toast.makeText(context, "Bạn chưa đăng nhập!", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendFavoriteRequest(context, course.getId(), userId);
                course.setRegistered(true);
                notifyItemChanged(position);
            } else {
                Toast.makeText(context, "Bạn đã đăng ký khoá học này rồi!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvCourseName, tvInstructor, tvDepartment;
        Button btnLessonCount;

        ImageView ivFavorite;
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvCourseName = itemView.findViewById(R.id.txt_ten_khoa_hoc);
            tvInstructor = itemView.findViewById(R.id.txt_giang_vien);
            tvDepartment = itemView.findViewById(R.id.txt_bo_mon);
            btnLessonCount = itemView.findViewById(R.id.btn_so_bai_hoc);
            ivFavorite = itemView.findViewById(R.id.btn_save);
        }
    }

    private void sendFavoriteRequest(Context context, String courseId, String userId) {
        String url = "http://14.225.207.221:6060/mobile/course-registrations";

        RequestQueue queue = Volley.newRequestQueue(context);

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("userId", userId);
            jsonBody.put("courseId", courseId);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> Toast.makeText(context, "Đã đăng ký khóa học!", Toast.LENGTH_SHORT).show(),
                error -> {
                    error.printStackTrace();
                    Toast.makeText(context, "Lỗi khi đăng ký khóa học!", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }


}
