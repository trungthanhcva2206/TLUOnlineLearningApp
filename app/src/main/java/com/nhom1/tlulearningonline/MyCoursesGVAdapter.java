package com.nhom1.tlulearningonline.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.tlulearningonline.R;
import com.nhom1.tlulearningonline.ChiTietKhoaHocActivity;
import com.nhom1.tlulearningonline.SuaKhoaHocActivity;
import com.nhom1.tlulearningonline.CourseItemGV;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesGVAdapter extends RecyclerView.Adapter<MyCoursesGVAdapter.CourseGVViewHolder> {
    private List<CourseItemGV> courseList;

    public MyCoursesGVAdapter(List<CourseItemGV> courseList) {
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public CourseGVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng layout item_khoa_hoc_gv_quan_ly.xml (layout CÓ nút edit/view)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khoa_hoc_gv_quan_ly, parent, false);
        return new CourseGVViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseGVViewHolder holder, int position) {
        CourseItemGV course = courseList.get(position);
        holder.tvTitle.setText(course.getTitle());
        holder.tvDescription.setText(course.getDescription());
        holder.btnLessonCount.setText(course.getLessonCount() + " bài học");

        // Bind và xử lý sự kiện cho các nút Edit và View
        holder.btnEdit.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Chỉnh sửa khóa học: " + course.getTitle(), Toast.LENGTH_SHORT).show();
            // TODO: Mở SuaKhoaHocActivity với dữ liệu của khóa học này
            Intent intent = new Intent(v.getContext(), SuaKhoaHocActivity.class);
            // Truyền dữ liệu khóa học qua intent nếu cần
            v.getContext().startActivity(intent);
        });

        holder.btnView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Xem chi tiết khóa học: " + course.getTitle(), Toast.LENGTH_SHORT).show();
            // TODO: Mở ChiTietKhoaHocActivity với dữ liệu của khóa học này
            Intent intent = new Intent(v.getContext(), ChiTietKhoaHocActivity.class); // Hoặc một activity chi tiết GV riêng
            // Truyền dữ liệu khóa học qua intent nếu cần
            // Ví dụ: intent.putExtra("tieu_de", course.getTitle());
            v.getContext().startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Mở khóa học: " + course.getTitle(), Toast.LENGTH_SHORT).show();
            // Intent để mở ChiTietKhoaHocActivity
            Intent intent = new Intent(v.getContext(), ChiTietKhoaHocActivity.class); // Hoặc một activity chi tiết GV riêng
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
        ImageView btnEdit, btnView; // CÓ CÁC NÚT NÀY

        public CourseGVViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_tieu_de_gv);
            tvDescription = itemView.findViewById(R.id.tv_mo_ta_gv);
            btnLessonCount = itemView.findViewById(R.id.btn_so_bai_gv);
            btnEdit = itemView.findViewById(R.id.btn_edit_gv); // Tìm kiếm nút edit
            btnView = itemView.findViewById(R.id.btn_view_gv);   // Tìm kiếm nút view
        }
    }
}
