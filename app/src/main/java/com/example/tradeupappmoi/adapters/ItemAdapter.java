package com.example.tradeupappmoi.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradeupappmoi.R;
import com.example.tradeupappmoi.models.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private final List<Item> items;

    public ItemAdapter(List<Item> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtPrice, txtCategory, txtLocation;  // Thêm TextView cho location
        public ImageView imgProduct;

        public ViewHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtPrice = view.findViewById(R.id.txtPrice);
            txtCategory = view.findViewById(R.id.txtCategory);
            txtLocation = view.findViewById(R.id.txtLocation);  // Lấy TextView location
            imgProduct = view.findViewById(R.id.imgProduct);
        }
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.txtTitle.setText(item.getTitle());
        holder.txtPrice.setText("Price: $" + item.getPrice());
        holder.txtCategory.setText("Category: " + item.getCategory());

        // Hiển thị thông tin location
        holder.txtLocation.setText("Location: " + item.getLocation());  // Hiển thị location

        Glide.with(holder.itemView.getContext())
                .load(item.getImage())
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}