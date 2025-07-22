package com.example.tradeupappmoi.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tradeupappmoi.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AddItemFragment extends Fragment {

    private EditText etTitle, etDescription, etPrice;
    private Spinner spCategory, spCondition;

    public AddItemFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_item, container, false);
        // Ánh xạ các view
        etTitle = rootView.findViewById(R.id.etTitle);
        etDescription = rootView.findViewById(R.id.etDescription);
        etPrice = rootView.findViewById(R.id.etPrice);
        spCategory = rootView.findViewById(R.id.spCategory);
        spCondition = rootView.findViewById(R.id.spCondition);
        Button btnSave = rootView.findViewById(R.id.btnSave);
        TextView tvLocation = rootView.findViewById(R.id.tvLocation);

        // Khởi tạo Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("product_images");

        @SuppressLint("UseRequireInsteadOfGet") ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),
                R.array.product_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(categoryAdapter);

        // Cấu hình Spinner cho Condition
        ArrayAdapter<CharSequence> conditionAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.product_conditions, android.R.layout.simple_spinner_item);
        conditionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCondition.setAdapter(conditionAdapter);
        // Lắng nghe sự kiện click nút chọn ảnh

        // Lắng nghe sự kiện click nút lưu sản phẩm
        btnSave.setOnClickListener(v -> saveProduct());

        // Hiển thị vị trí sản phẩm (có thể sử dụng GPS hoặc yêu cầu người dùng nhập thủ công)
        tvLocation.setText("Vị trí: Hà Nội"); // Ví dụ: Bạn có thể lấy vị trí từ GPS

        return rootView;
    }

    // Mở thư viện ảnh để người dùng chọn ảnh
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1000); // Hệ số yêu cầu cho chọn ảnh
    }

    // Lưu sản phẩm vào Firestore và Firebase Storage
    private void saveProduct() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String price = etPrice.getText().toString().trim();

        // Kiểm tra nếu các Spinner có giá trị hợp lệ
        String category = spCategory.getSelectedItem() != null ? spCategory.getSelectedItem().toString() : "";
        String condition = spCondition.getSelectedItem() != null ? spCondition.getSelectedItem().toString() : "";

        // Log các giá trị để kiểm tra
        Log.d("AddItemFragment", "Title: " + title);
        Log.d("AddItemFragment", "Description: " + description);
        Log.d("AddItemFragment", "Price: " + price);
        Log.d("AddItemFragment", "Category: " + category);
        Log.d("AddItemFragment", "Condition: " + condition);

        // Kiểm tra nếu tất cả thông tin đã được nhập đầy đủ
        if (title.isEmpty() || description.isEmpty() || price.isEmpty() || category.isEmpty() || condition.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            return;  // Dừng hàm nếu có trường trống
        }

        // Kiểm tra nếu giá không phải là số hợp lệ và giá phải lớn hơn 0
        if (!isValidPrice(price) || Double.parseDouble(price) <= 0) {
            Toast.makeText(getContext(), "Giá phải là một số hợp lệ và lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chỉ lưu thông tin sản phẩm mà không cần ảnh
        saveToFirestore(title, description, price, category, condition);
    }

    // Lưu sản phẩm vào Firestore
    private void saveToFirestore(String title, String description, String price, String category, String condition) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> product = new HashMap<>();
        product.put("title", title);
        product.put("description", description);
        product.put("price", price);
        product.put("category", category);
        product.put("condition", condition);

        // Lưu vào Firestore
        db.collection("products")
                .add(product)
                .addOnSuccessListener(documentReference -> Toast.makeText(getContext(), "Sản phẩm đã được lưu", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi khi lưu sản phẩm", Toast.LENGTH_SHORT).show());
    }


    private boolean isValidPrice(String price) {
        try {
            Double.parseDouble(price);  // Kiểm tra xem giá có phải là số hợp lệ
            return true;
        } catch (NumberFormatException e) {
            return false;  // Nếu không phải số hợp lệ, trả về false
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == Activity.RESULT_OK) {
            // Để lưu trữ ảnh được chọn
            Uri imageUri = data.getData(); // Lấy URI của ảnh đã chọn
            if (imageUri == null) {
                Toast.makeText(getContext(), "Không có ảnh được chọn", Toast.LENGTH_SHORT).show();
            }
        }
    }
}