package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

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
    }

    // Trong file TopCourseAdapter.java
    public void filterList(List<CourseItem> filteredList) {
        this.courseList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvCourseName, tvInstructor, tvDepartment;
        Button btnLessonCount;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvCourseName = itemView.findViewById(R.id.txt_ten_khoa_hoc);
            tvInstructor = itemView.findViewById(R.id.txt_giang_vien);
            tvDepartment = itemView.findViewById(R.id.txt_bo_mon);
            btnLessonCount = itemView.findViewById(R.id.btn_so_bai_hoc);
        }
    }
}
