package com.example.tradeupappmoi.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tradeupappmoi.R;
import com.example.tradeupappmoi.adapters.MessageAdapter;
import com.example.tradeupappmoi.models.Message;  // Sửa import cho đúng class Message

import java.util.ArrayList;
import java.util.List;

public class MessagesFragment extends Fragment {

    private EditText messageInput;
    private MessageAdapter messageAdapter;
    private final List<Message> messageList = new ArrayList<>();

    public MessagesFragment() {
        // Constructor mặc định
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);

        RecyclerView messagesRecyclerView = rootView.findViewById(R.id.messagesRecyclerView);
        messageInput = rootView.findViewById(R.id.messageInput);
        ImageView sendButton = rootView.findViewById(R.id.sendButton);

        // Cài đặt RecyclerView với LinearLayoutManager
        messageAdapter = new MessageAdapter(messageList);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // Thêm LinearLayoutManager
        messagesRecyclerView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> sendMessage());

        return rootView;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Tạo đối tượng Message từ dữ liệu nhập vào
            Message message = new Message("userId", messageText, System.currentTimeMillis());
            messageList.add(message);
            messageAdapter.notifyDataSetChanged(); // Cập nhật giao diện
            messageInput.setText(""); // Xóa nội dung sau khi gửi

            // Tích hợp Firebase hoặc backend để lưu tin nhắn
            // FirebaseHelper.sendMessage(message);
        }
    }
}
