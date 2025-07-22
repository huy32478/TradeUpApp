package com.example.tradeupappmoi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradeupappmoi.R;
import com.example.tradeupappmoi.models.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final List<Message> messageList;

    public MessageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout cho mỗi item tin nhắn
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        // Lấy tin nhắn tại vị trí hiện tại
        Message message = messageList.get(position);
        holder.messageText.setText(message.getMessage());
        // Có thể thêm thời gian hoặc tên người dùng nếu cần
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        public MessageViewHolder(View itemView) {
            super(itemView);
            // Liên kết với view trong item_message.xml
            messageText = itemView.findViewById(R.id.messageText);
        }
    }
}
