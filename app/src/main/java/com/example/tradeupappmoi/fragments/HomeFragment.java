package com.example.tradeupappmoi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tradeupappmoi.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Constructor rỗng bắt buộc
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout cho fragment (bạn cần tạo home_fragment.xml nếu chưa có)
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}