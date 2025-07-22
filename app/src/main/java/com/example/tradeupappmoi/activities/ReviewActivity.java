package com.example.tradeupappmoi.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradeupappmoi.R;
import com.example.tradeupappmoi.adapters.ReviewAdapter;
import com.example.tradeupappmoi.models.Review;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReviewActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private final String productId = "example_product_id"; // bạn sẽ truyền đúng ID vào sau
    private final List<Review> reviewList = new ArrayList<>();
    private ReviewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.reviewRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ReviewAdapter(reviewList);
        recyclerView.setAdapter(adapter);

        RatingBar ratingBar = findViewById(R.id.ratingBarInput);
        EditText edtReview = findViewById(R.id.edtReview);
        Button btnSubmit = findViewById(R.id.btnSubmitReview);

        btnSubmit.setOnClickListener(v -> {
            String content = edtReview.getText().toString();
            float rating = ratingBar.getRating();

            if (TextUtils.isEmpty(content) || rating == 0) {
                Toast.makeText(this, "Điền đầy đủ nội dung và sao!", Toast.LENGTH_SHORT).show();
                return;
            }

            String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName(); // hoặc lưu trong profile
            String reviewId = db.collection("reviews").document().getId();

            Review review = new Review(reviewId, productId, uid, userName, content, rating, System.currentTimeMillis());

            db.collection("reviews").document(reviewId).set(review)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Đánh giá đã được gửi!", Toast.LENGTH_SHORT).show();
                        reviewList.add(0, review);
                        adapter.notifyItemInserted(0);
                        edtReview.setText("");
                        ratingBar.setRating(0);
                    });
        });

        loadReviews();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadReviews() {
        db.collection("reviews")
                .whereEqualTo("productId", productId)
                .orderBy("timestamp")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    reviewList.clear();
                    for (var doc : querySnapshot) {
                        Review r = doc.toObject(Review.class);
                        reviewList.add(r);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
