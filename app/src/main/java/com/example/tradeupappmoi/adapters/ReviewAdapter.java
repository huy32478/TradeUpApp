package com.example.tradeupappmoi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradeupappmoi.R;
import com.example.tradeupappmoi.models.Review;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final List<Review> reviews;

    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtUserName, txtContent;
        RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtContent = itemView.findViewById(R.id.txtContent);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.txtUserName.setText(review.getUserName());
        holder.txtContent.setText(review.getContent());
        holder.ratingBar.setRating(review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
