package com.nhom1.tlulearningonline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;

import java.util.List;

import com.nhom1.tlulearningonline.R;
import com.nhom1.tlulearningonline.KhoaHoc;

public class KhoaHocAdapter extends RecyclerView.Adapter<KhoaHocAdapter.KhoaHocViewHolder> {

    private final List<KhoaHoc> danhSachKhoaHoc;

    public KhoaHocAdapter(List<KhoaHoc> danhSachKhoaHoc) {
        this.danhSachKhoaHoc = danhSachKhoaHoc;
    }

    @NonNull
    @Override
    public KhoaHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_khoa_hoc_trang_chu, parent, false);
        return new KhoaHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhoaHocViewHolder holder, int position) {
        KhoaHoc khoaHoc = danhSachKhoaHoc.get(position);
        holder.tvTenKhoaHoc.setText(khoaHoc.getTenKhoaHoc());
        holder.tvTenGiangVien.setText("GV: " + khoaHoc.getTenGiangVien());
        holder.btn_so_bai.setText(khoaHoc.getSoBai() + " bài");

        // Màu nền so le từ @color áp dụng cho CardView để giữ bo góc
        int colorResId = (position % 2 == 0) ? R.color.blue : R.color.blue_3;
        int bgColor = ContextCompat.getColor(holder.itemView.getContext(), colorResId);
        holder.cardView.setCardBackgroundColor(bgColor);
    }

    @Override
    public int getItemCount() {
        return danhSachKhoaHoc.size();
    }

    public static class KhoaHocViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenKhoaHoc, tvTenGiangVien, tvBoMon;
        Button btn_so_bai;
        CardView cardView;

        public KhoaHocViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenKhoaHoc = itemView.findViewById(R.id.txt_ten_khoa_hoc);
            tvTenGiangVien = itemView.findViewById(R.id.txt_giang_vien);
            btn_so_bai = itemView.findViewById(R.id.btn_so_bai);
            cardView = (CardView) itemView;
        }
    }
}