package com.nhom1.tlulearningonline;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BaiHocAdapter extends RecyclerView.Adapter<BaiHocAdapter.BaiHocViewHolder> {
    private List<Lesson> danhSachBaiHoc; // Thay đổi từ List<String> thành List<Lesson>
    private Context context;

    public BaiHocAdapter(List<Lesson> danhSachBaiHoc, Context context) {
        this.danhSachBaiHoc = danhSachBaiHoc;
        this.context = context;
    }

    @NonNull
    @Override
    public BaiHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bai_hoc, parent, false);
        return new BaiHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaiHocViewHolder holder, int position) {
        Lesson baiHoc = danhSachBaiHoc.get(position);
        holder.tvTieuDeBaiHoc.setText(baiHoc.getTitle());
        holder.tvThoiLuong.setText(baiHoc.getDuration());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ChiTietBaiHocActivity.class);
            intent.putExtra("tieuDe", baiHoc.getTitle());
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return danhSachBaiHoc.size();
    }

    public static class BaiHocViewHolder extends RecyclerView.ViewHolder {
        TextView tvTieuDeBaiHoc;
        TextView tvThoiLuong;

        public BaiHocViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTieuDeBaiHoc = itemView.findViewById(R.id.tv_lesson_title);
            tvThoiLuong = itemView.findViewById(R.id.tv_lesson_duration);
        }
    }
}