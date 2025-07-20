package com.example.tradeupappmoi.activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tradeupappmoi.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewUserProfileActivity extends AppCompatActivity {

    private ImageView imgProfileOther;
    private TextView tvDisplayNameOther, tvBioOther, tvContactOther, tvRatingOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_profile);

        imgProfileOther = findViewById(R.id.imgProfileOther);
        tvDisplayNameOther = findViewById(R.id.tvDisplayNameOther);
        tvBioOther = findViewById(R.id.tvBioOther);
        tvContactOther = findViewById(R.id.tvContactOther);
        tvRatingOther = findViewById(R.id.tvRatingOther);

        String userId = getIntent().getStringExtra("userId");
        if (userId != null) {
            loadUserProfile(userId);
        } else {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadUserProfile(String uid) {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        String name = snapshot.getString("displayName");
                        String bio = snapshot.getString("bio");
                        String contact = snapshot.getString("contact");
                        String imgUrl = snapshot.getString("profilePicUrl");
                        Double rating = snapshot.getDouble("rating");

                        tvDisplayNameOther.setText(name != null ? name : "N/A");
                        tvBioOther.setText(bio != null ? bio : "No bio provided");
                        tvContactOther.setText(contact != null ? contact : "No contact info");
                        tvRatingOther.setText(rating != null ? "Rating: " + rating : "Rating: N/A");

                        if (imgUrl != null && !imgUrl.isEmpty()) {
                            Glide.with(this).load(imgUrl).into(imgProfileOther);
                        } else {
                            imgProfileOther.setImageResource(R.drawable.ic_default_avatar); // fallback image
                        }
                    } else {
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load user", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}

