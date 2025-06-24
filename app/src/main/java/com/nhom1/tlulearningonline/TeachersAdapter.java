package com.nhom1.tlulearningonline; // Ensure this matches your project's package name

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast; // Used for example click listener

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Adapter for displaying a list of teachers in a RecyclerView.
 * This adapter binds TeacherItem data to the item_teacher_avatar.xml layout.
 */
public class TeachersAdapter extends RecyclerView.Adapter<TeachersAdapter.TeacherViewHolder> {

    private List<TeacherItem> teacherList;

    /**
     * Constructor for the TeachersAdapter.
     * @param teacherList The list of TeacherItem objects to display.
     */
    public TeachersAdapter(List<TeacherItem> teacherList) {
        this.teacherList = teacherList;
    }

    /**
     * Called when RecyclerView needs a new {@link TeacherViewHolder} of the given type to represent an item.
     * This method inflates the layout for individual teacher items.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     * an adapter position.
     * @param viewType The view type of the new View.
     * @return A new TeacherViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_teacher_avatar.xml layout for each teacher item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_avatar, parent, false);
        return new TeacherViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * This method updates the contents of the {@link TeacherViewHolder#itemView} to reflect the item at the given
     * position.
     *
     * @param holder   The TeacherViewHolder which should be updated to represent the contents of the
     * item at the given `position` in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        TeacherItem teacher = teacherList.get(position);

        // Set the teacher's name
        holder.tvTeacherName.setText(teacher.getName());

        // Set the teacher's avatar image using the drawable resource ID
        Glide.with(holder.itemView.getContext())
                .load(teacher.getAvatarResId())
                .placeholder(R.drawable.ic_avatar)
                .error(R.drawable.ic_avatar)
                .circleCrop()
                .into(holder.ivTeacherAvatar);

        // Set an onClickListener for the entire item view
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "View teacher profile: " + teacher.getName(), Toast.LENGTH_SHORT).show();
            // TODO: Implement navigation to a specific teacher profile activity here.
            // Example: Intent intent = new Intent(v.getContext(), TeacherProfileActivity.class);
            //          intent.putExtra("teacher_name", teacher.getName());
            //          v.getContext().startActivity(intent);
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    /**
     * ViewHolder for the teacher items in the RecyclerView.
     * Holds references to the views within each item_teacher_avatar.xml layout.
     */
    public static class TeacherViewHolder extends RecyclerView.ViewHolder {
        ImageView ivTeacherAvatar;
        TextView tvTeacherName;

        /**
         * Constructor for the ViewHolder.
         * @param itemView The root view of the item layout (item_teacher_avatar.xml).
         */
        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from the item_teacher_avatar.xml layout
            ivTeacherAvatar = itemView.findViewById(R.id.iv_teacher_avatar);
            tvTeacherName = itemView.findViewById(R.id.tv_teacher_name);
        }
    }
}
