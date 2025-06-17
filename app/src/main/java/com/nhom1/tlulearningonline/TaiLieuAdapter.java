package com.nhom1.tlulearningonline;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TaiLieuAdapter extends RecyclerView.Adapter<TaiLieuAdapter.TaiLieuViewHolder> {

    private ArrayList<String> taiLieuList;
    private Context context;

    public TaiLieuAdapter(ArrayList<String> taiLieuList, Context context) {
        this.taiLieuList = taiLieuList;
        this.context = context;
    }

    @NonNull
    @Override
    public TaiLieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tai_lieu, parent, false);
        return new TaiLieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaiLieuViewHolder holder, int position) {
        String tenTaiLieu = taiLieuList.get(position);
        holder.tvTenTaiLieu.setText(tenTaiLieu);

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Mở " + tenTaiLieu, Toast.LENGTH_SHORT).show();
            // TODO: Mở tài liệu PDF bằng Intent
        });
    }

    @Override
    public int getItemCount() {
        return taiLieuList.size();
    }

    public static class TaiLieuViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenTaiLieu;

        public TaiLieuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenTaiLieu = itemView.findViewById(R.id.tvTenTaiLieu);
        }
    }
}

