package com.example.tradeupappmoi.firebase;

import android.os.Message;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference messagesRef = db.collection("messages");

    public void sendMessage(Message message) {
        messagesRef.add(message)
                .addOnSuccessListener(documentReference -> Log.d("Firebase", "Tin nhắn đã được gửi"))
                .addOnFailureListener(e -> Log.e("Firebase", "Lỗi gửi tin nhắn", e));
    }
}