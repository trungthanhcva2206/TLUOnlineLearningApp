package com.nhom1.tlulearningonline.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nhom1.tlulearningonline.R;
import com.nhom1.tlulearningonline.model.Account;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    private List<Account> accountList;
    private OnAccountClickListener listener;

    public interface OnAccountClickListener {
        void onChiTietClick(Account account);
    }

    public AccountAdapter(List<Account> accountList, OnAccountClickListener listener) {
        this.accountList = accountList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_account, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        Account acc = accountList.get(position);
        holder.tvMsv.setText(acc.getMsv());
        holder.tvFullName.setText(acc.getFullName());
        holder.tvRole.setText(acc.getRole());
        holder.tvClassName.setText(acc.getClassName());
        holder.tvChiTiet.setOnClickListener(v -> listener.onChiTietClick(acc));
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView tvMsv, tvFullName, tvRole, tvClassName, tvChiTiet;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMsv = itemView.findViewById(R.id.tvMsv);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvClassName = itemView.findViewById(R.id.tvClassName);
            tvChiTiet = itemView.findViewById(R.id.tvChiTiet);
        }
    }
}

