package com.nhom1.tlulearningonline;

public class Message {
    private String id;
    private String senderId;
    private String senderName;
    private String senderAvatar;
    private String content;
    private String createdAt;

    public Message() {}

    public Message(String id, String content, String senderName, String senderAvatar, String createdAt) {
        this.id = id;
        this.content = content;
        this.senderName = senderName;
        this.senderAvatar = senderAvatar;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
