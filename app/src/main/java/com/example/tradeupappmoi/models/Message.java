package com.example.tradeupappmoi.models;

public class Message {
    private final String userId;
    private final String message;
    private final long timestamp;

    public Message(String userId, String message, long timestamp) {
        this.userId = userId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

