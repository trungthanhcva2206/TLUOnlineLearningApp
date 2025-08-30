package com.nhom1.tlulearningonline;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaiLieuAdapter extends RecyclerView.Adapter<TaiLieuAdapter.ViewHolder> {
    private List<Document> taiLieuList;
    private Context context;

    public TaiLieuAdapter(List<Document> taiLieuList, Context context) {
        this.taiLieuList = taiLieuList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tai_lieu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Document doc = taiLieuList.get(position);
        holder.tvTaiLieu.setText(doc.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(doc.getUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taiLieuList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaiLieu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaiLieu = itemView.findViewById(R.id.tvTaiLieuTen);
        }
    }
}


