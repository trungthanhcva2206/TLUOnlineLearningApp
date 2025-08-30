package com.nhom1.tlulearningonline;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList; // Import ArrayList
import java.util.List;

import com.bumptech.glide.Glide;
import com.nhom1.tlulearningonline.R;
import com.nhom1.tlulearningonline.GiangVien;

public class GiangVienAdapter extends RecyclerView.Adapter<GiangVienAdapter.GiangVienViewHolder> {

    private List<GiangVien> danhSachGiangVien;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(GiangVien giangVien);
    }

    public GiangVienAdapter(List<GiangVien> danhSachGiangVien, OnItemClickListener listener) {
        this.danhSachGiangVien = danhSachGiangVien;
        this.listener = listener;
    }

    @NonNull
    @Override
    public GiangVienViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_giang_vien, parent, false);
        return new GiangVienViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GiangVienViewHolder holder, int position) {
        GiangVien giangVien = danhSachGiangVien.get(position);
        holder.tvTenGiangVien.setText(giangVien.getFullname());

        Glide.with(holder.ivAvatar.getContext())
                .load(giangVien.getAvatarUrl())
                .placeholder(R.drawable.ic_avatar)
                .error(R.drawable.ic_avatar)
                .circleCrop()
                .into(holder.ivAvatar);

        // Thiết lập màu nền so le theo vị trí chẵn lẻ - áp dụng cho CardView để giữ bo góc
        int backgroundColorResId = (position % 2 == 0) ? R.color.blue : R.color.blue_3;
        int color = ContextCompat.getColor(holder.itemView.getContext(), backgroundColorResId);
        holder.cardView.setCardBackgroundColor(color);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(giangVien));
    }

    @Override
    public int getItemCount() {
        return danhSachGiangVien.size();
    }

    // --- BẠN CẦN THÊM PHƯƠNG THỨC NÀY VÀO ---
    /**
     * Cập nhật danh sách hiển thị trong adapter với một danh sách đã được lọc.
     * @param filteredList Danh sách giảng viên đã được lọc.
     */
    public void filterList(List<GiangVien> filteredList) {
        this.danhSachGiangVien = filteredList;
        notifyDataSetChanged();
    }
    // ------------------------------------------

    public static class GiangVienViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvTenGiangVien, tvBoMon;
        CardView cardView;

        public GiangVienViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            // ID của TextView tên giảng viên có vẻ không khớp, tôi đã sửa lại thành R.id.tv_ten_giang_vien
            // Nếu ID của bạn là tvTenGiangVien, hãy đổi lại cho đúng.
            tvTenGiangVien = itemView.findViewById(R.id.tvTenGiangVien);
            tvBoMon = itemView.findViewById(R.id.tv_bo_mon);
            cardView = (CardView) itemView;
        }
    }
}