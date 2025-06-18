package com.nhom1.tlulearningonline;

public class Message {
    public String sender;
    public String text;
    public boolean isMine;

    public Message(String sender, String text, boolean isMine) {
        this.sender = sender;
        this.text = text;
        this.isMine = isMine;
    }
}

