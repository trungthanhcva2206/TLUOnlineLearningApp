package com.nhom1.tlulearningonline;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {
    private RecyclerView recyclerMessages;
    private EditText etMessage;
    private ImageButton btnSend;
    private List<Message> messageList;
    private MessageAdapter adapter;
    private ImageView btnBack;
    private TextView roomStatus;

    private static final String BASE_URL = "http://14.225.207.221:6060/mobile/messages";
    private static final String USER_URL = "http://14.225.207.221:6060/mobile/users";
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        recyclerMessages = findViewById(R.id.recyclerMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);
        roomStatus = findViewById(R.id.room_status);

        messageList = new ArrayList<>();
        SessionManager session = new SessionManager(this);
        currentUserId = session.getUserId();

        adapter = new MessageAdapter(this, messageList, currentUserId);
        recyclerMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerMessages.setAdapter(adapter);

        fetchUserCount();
        startFetchingMessagesLoop(); // Tự động lấy tin nhắn liên tục

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessageToServer(currentUserId, text);
                etMessage.setText("");
            }
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(GroupChatActivity.this, MainActivity.class));
            finish();
        });
    }

    private void startFetchingMessagesLoop() {
        fetchMessagesFromServer();
        recyclerMessages.postDelayed(this::startFetchingMessagesLoop, 3000); // 3 giây
    }

    private void fetchMessagesFromServer() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                BASE_URL,
                null,
                response -> {
                    try {
                        messageList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            Message message = new Message();
                            message.setId(obj.getString("id"));
                            message.setSenderId(obj.getString("senderId")); // ĐÃ CÓ từ API
                            message.setSenderName(obj.getString("senderName"));
                            message.setSenderAvatar(obj.getString("senderAvatar"));
                            message.setContent(obj.getString("content"));
                            message.setCreatedAt(obj.getString("createdAt"));
                            messageList.add(message);
                        }
                        adapter.notifyDataSetChanged();
                        recyclerMessages.scrollToPosition(messageList.size() - 1);
                    } catch (Exception e) {
                        Log.e("FetchMessages", "JSON error: " + e.getMessage());
                    }
                },
                error -> Log.e("Volley", "Fetch error: " + error.toString())
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void sendMessageToServer(String senderId, String content) {
        try {
            JSONObject json = new JSONObject();
            json.put("senderId", senderId);
            json.put("content", content);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    BASE_URL,
                    json,
                    response -> {
                        try {
                            Message message = new Message();
                            message.setId(response.getString("id"));
                            message.setSenderId(senderId);
                            message.setSenderName(response.getString("senderName"));
                            message.setSenderAvatar(response.getString("senderAvatar"));
                            message.setContent(response.getString("content"));
                            message.setCreatedAt(response.getString("createdAt"));

                            messageList.add(message);
                            adapter.notifyItemInserted(messageList.size() - 1);
                            recyclerMessages.scrollToPosition(messageList.size() - 1);
                        } catch (Exception e) {
                            Log.e("SendMessage", "Parse error: " + e.getMessage());
                        }
                    },
                    error -> Log.e("Volley", "Send error: " + error.toString())
            );

            VolleySingleton.getInstance(this).addToRequestQueue(request);
        } catch (Exception e) {
            Log.e("SendMessage", "Exception: " + e.getMessage());
        }
    }
    private void fetchUserCount() {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                USER_URL,
                null,
                response -> {
                    int userCount = response.length();
                    roomStatus.setText(userCount + " members");
                },
                error -> Log.e("Volley", "User count error: " + error.toString())
        );

        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }
}
