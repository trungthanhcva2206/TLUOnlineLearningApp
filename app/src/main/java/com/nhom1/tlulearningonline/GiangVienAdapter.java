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

import java.util.List;

import com.nhom1.tlulearningonline.R;
import com.nhom1.tlulearningonline.GiangVien;

public class GiangVienAdapter extends RecyclerView.Adapter<GiangVienAdapter.GiangVienViewHolder> {

    private final List<GiangVien> danhSachGiangVien;
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
        holder.tvTenGiangVien.setText(giangVien.getTen());
        holder.tvBoMon.setText("Bộ môn: "+giangVien.getBoMon());
        holder.ivAvatar.setImageResource(giangVien.getAvatarResId());

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

    public static class GiangVienViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAvatar;
        TextView tvTenGiangVien, tvBoMon;
        CardView cardView;

        public GiangVienViewHolder(@NonNull View itemView) {
            super(itemView);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvTenGiangVien = itemView.findViewById(R.id.tvTenGiangVien);
            tvBoMon = itemView.findViewById(R.id.tvBoMon);
            cardView = (CardView) itemView;
        }
    }
}

