package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.tlulearningonline.ChiTietKhoaHocActivity;
import com.nhom1.tlulearningonline.R;
import com.nhom1.tlulearningonline.CourseItemGV;

import java.util.List;

public class FeaturedCoursesGVAdapter extends RecyclerView.Adapter<FeaturedCoursesGVAdapter.CourseGVViewHolder> {
    private List<CourseItemGV> courseList;

    public FeaturedCoursesGVAdapter(List<CourseItemGV> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseGVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout item_khoa_hoc_gv.xml (layout KHÔNG có nút edit/view)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khoa_hoc_gv, parent, false);
        return new CourseGVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseGVViewHolder holder, int position) {
        CourseItemGV course = courseList.get(position);
        holder.tvTitle.setText(course.getTitle());
        holder.tvDescription.setText(course.getDescription());
        holder.btnLessonCount.setText(course.getLessonCount() + " bài học");

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ChiTietKhoaHocActivity.class);

            intent.putExtra("course_id", course.getId());
            intent.putExtra("tieu_de", course.getTitle());
            intent.putExtra("des", course.getDescription());
            intent.putExtra("tac_gia", course.getTeacherName());
            intent.putExtra("so_bai", course.getLessonCount());

            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class CourseGVViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription;
        Button btnLessonCount;
        // KHÔNG CÓ ImageView btnEdit, btnView ở đây

        public CourseGVViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_tieu_de_gv);
            tvDescription = itemView.findViewById(R.id.tv_mo_ta_gv);
            btnLessonCount = itemView.findViewById(R.id.btn_so_bai_gv);
        }
    }
}
