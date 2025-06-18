package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {
    private RecyclerView recyclerMessages;
    private EditText etMessage;
    private ImageButton btnSend;
    private List<Message> messageList;
    private MessageAdapter adapter;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        recyclerMessages = findViewById(R.id.recyclerMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerMessages.setAdapter(adapter);

        seedDummyMessages(); // thêm dữ liệu mẫu

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                messageList.add(new Message("Tôi", text, true));
                adapter.notifyItemInserted(messageList.size() - 1);
                etMessage.setText("");
            }
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(GroupChatActivity.this, MainActivity.class));
            finish(); // đóng trang chat
        });
    }

    private void seedDummyMessages() {
        messageList.add(new Message("Hung Anh - 2251061782", "This is my new 3d design", false));
        messageList.add(new Message("Hung Anh - 2251061782", "Nice a day", false));
        messageList.add(new Message("Hung Anh - 2251061782", "Be happy life be happy ending", false));
        messageList.add(new Message("Tôi", "You did your job well!", true));
        messageList.add(new Message("Trung Thanh - 2251061885", "Have a great working week!!", false));
        messageList.add(new Message("Trung Thanh - 2251061885", "Hope you like it", false));
        messageList.add(new Message("Tôi", "Hello! Trung Thanh", true));
    }

}
