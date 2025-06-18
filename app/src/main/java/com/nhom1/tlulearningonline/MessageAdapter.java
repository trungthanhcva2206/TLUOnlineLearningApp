package com.nhom1.tlulearningonline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messages;
    private static final int TYPE_MINE = 1;
    private static final int TYPE_OTHER = 2;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).isMine ? TYPE_MINE : TYPE_OTHER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MINE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_right, parent, false);
            return new RightViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_left, parent, false);
            return new LeftViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Message msg = messages.get(position);
        if (holder instanceof RightViewHolder) {
            ((RightViewHolder) holder).tvMessage.setText(msg.text);
        } else if (holder instanceof LeftViewHolder) {
            ((LeftViewHolder) holder).tvSender.setText(msg.sender);
            ((LeftViewHolder) holder).tvMessage.setText(msg.text);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class RightViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        RightViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }

    static class LeftViewHolder extends RecyclerView.ViewHolder {
        TextView tvSender, tvMessage;
        ImageView imgAvatar;
        LeftViewHolder(View itemView) {
            super(itemView);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }
    }
}

