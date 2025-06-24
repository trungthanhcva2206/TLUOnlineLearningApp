package com.nhom1.tlulearningonline;

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

import java.util.List;

/**
 * Adapter for displaying a list of normal courses (without progress bar) in a RecyclerView.
 * This adapter binds CourseItem data to the item_khoa_hoc_sv.xml layout.
 */
public class TopCourseAdapter extends RecyclerView.Adapter<TopCourseAdapter.CourseViewHolder> {

    private List<CourseItem> courseList;

    /**
     * Constructor for the TopCourseAdapter.
     * @param courseList The list of CourseItem objects to display.
     */
    public TopCourseAdapter(List<CourseItem> courseList) {
        this.courseList = courseList;
    }

    /**
     * Called when RecyclerView needs a new {@link CourseViewHolder} of the given type to represent an item.
     * This method inflates the layout for individual course items (item_khoa_hoc_sv.xml).
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new CourseViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_khoa_hoc_sv.xml layout for each course item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khoa_hoc_sv, parent, false);
        return new CourseViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method updates the contents of the {@link CourseViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The CourseViewHolder which should be updated to represent the contents of the
     * item at the given `position` in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseItem course = courseList.get(position);

        holder.tvCourseName.setText(course.getTitle());
        holder.tvInstructor.setText("GV: " + course.getInstructor());
        holder.tvDepartment.setText("Bộ môn: " + course.getDepartment());
        holder.btnLessonCount.setText(course.getSoBaiHoc() + " bài học"); // Bind the lesson count button

        // Set the star icon based on the isStarred status
        if (course.isStarred()) {
            holder.btnSave.setImageResource(R.drawable.ic_star_filled);
        } else {
            holder.btnSave.setImageResource(R.drawable.ic_star_border);
        }

        // Set alternating background colors for the CardView
        int colorId = (position % 2 == 0) ? R.color.blue_3 : R.color.blue;
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), colorId));

        // Click listener for the entire item view (e.g., to open course details)
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Mở khóa học: " + course.getTitle(), Toast.LENGTH_SHORT).show();
            // TODO: Implement navigation to ChiTietKhoaHocActivity or a similar course detail activity
            // Example: Intent intent = new Intent(v.getContext(), ChiTietKhoaHocActivity.class);
            //          intent.putExtra("course_title", course.getTitle());
            //          // Pass other course details if needed
            //          v.getContext().startActivity(intent);
        });

        // Click listener for the star/save button
        holder.btnSave.setOnClickListener(v -> {
            // Toggle the starred status and update the icon
            course.setStarred(!course.isStarred());
            if (course.isStarred()) {
                holder.btnSave.setImageResource(R.drawable.ic_star_filled);
                Toast.makeText(v.getContext(), "Đã lưu khóa học: " + course.getTitle(), Toast.LENGTH_SHORT).show();
            } else {
                holder.btnSave.setImageResource(R.drawable.ic_star_border);
                Toast.makeText(v.getContext(), "Đã bỏ lưu khóa học: " + course.getTitle(), Toast.LENGTH_SHORT).show();
            }
            // TODO: Persist this change (e.g., update in database or shared preferences)
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return courseList.size();
    }

    /**
     * ViewHolder for the course items in the RecyclerView.
     * Holds references to the views within each item_khoa_hoc_sv.xml layout.
     */
    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvCourseName, tvInstructor, tvDepartment;
        Button btnLessonCount; // This is the button for lesson count
        ImageView btnSave;

        /**
         * Constructor for the ViewHolder.
         * @param itemView The root view of the item layout (item_khoa_hoc_sv.xml).
         */
        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            tvCourseName = itemView.findViewById(R.id.txt_ten_khoa_hoc);
            tvInstructor = itemView.findViewById(R.id.txt_giang_vien);
            tvDepartment = itemView.findViewById(R.id.txt_bo_mon);
            btnLessonCount = itemView.findViewById(R.id.btn_so_bai_hoc); // Find the lesson count button
            btnSave = itemView.findViewById(R.id.btn_save);
        }
    }
}