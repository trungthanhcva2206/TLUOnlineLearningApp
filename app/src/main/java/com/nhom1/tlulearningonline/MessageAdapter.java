package com.nhom1.tlulearningonline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SENT = 1;
    private static final int TYPE_RECEIVED = 2;

    private final List<Message> messageList;
    private final Context context;
    private final String currentUserId;

    public MessageAdapter(Context context, List<Message> messageList, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messageList.get(position);
        return message.getSenderId().equals(currentUserId) ? TYPE_SENT : TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_SENT) {
            View view = inflater.inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder instanceof SentMessageHolder) {
            ((SentMessageHolder) holder).bind(message);
        } else {
            ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }

        public void bind(Message message) {
            tvMessage.setText(message.getContent());
        }
    }

    static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView tvSender, tvMessage;
        ImageView imgAvatar;

        public ReceivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            tvSender = itemView.findViewById(R.id.tvSender);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }

        public void bind(Message message) {
            tvSender.setText(message.getSenderName());
            tvMessage.setText(message.getContent());

            Glide.with(itemView.getContext())
                    .load(message.getSenderAvatar())
                    .placeholder(R.drawable.profile)
                    .error(R.drawable.profile)
                    .into(imgAvatar);
        }
    }
}
