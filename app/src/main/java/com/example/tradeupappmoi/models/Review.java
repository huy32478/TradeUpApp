package com.example.tradeupappmoi.models;

public class Review {
    private String reviewId;
    private String productId;
    private String userId;
    private String userName;
    private String content;
    private float rating;
    private long timestamp;

    public Review() {} // Bắt buộc với Firestore

    public Review(String reviewId, String productId, String userId, String userName, String content, float rating, long timestamp) {
        this.reviewId = reviewId;
        this.productId = productId;
        this.userId = userId;
        this.userName = userName;
        this.content = content;
        this.rating = rating;
        this.timestamp = timestamp;
    }

    // Getters
    public String getReviewId() { return reviewId; }
    public String getProductId() { return productId; }
    public String getUserId() { return userId; }
    public String getUserName() { return userName; }
    public String getContent() { return content; }
    public float getRating() { return rating; }
    public long getTimestamp() { return timestamp; }

}

