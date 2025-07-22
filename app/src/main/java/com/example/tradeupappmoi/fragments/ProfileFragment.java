package com.example.tradeupappmoi.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tradeupappmoi.R;
import com.example.tradeupappmoi.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private ImageView imgProfile;
    private EditText etDisplayName, etBio, etContact;
    private RatingBar ratingBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageRef;

    private Uri selectedImageUri;
    private String currentImageUrl;

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        requireContext().getContentResolver().takePersistableUriPermission(
                                selectedImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Glide.with(this).load(selectedImageUri).into(imgProfile);
                        Log.d("ProfileFragment", "Selected image URI: " + selectedImageUri.toString());
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgProfile = view.findViewById(R.id.imgProfile);
        etDisplayName = view.findViewById(R.id.etDisplayName);
        etBio = view.findViewById(R.id.etBio);
        etContact = view.findViewById(R.id.etContact);
        ratingBar = view.findViewById(R.id.ratingBar);

        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnDeactivate = view.findViewById(R.id.btnDeactivate);
        Button btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference("profile_images");

        loadUserProfile();


        btnSave.setOnClickListener(v -> saveUserProfile());

        // Hủy kích hoạt tài khoản
        btnDeactivate.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null && !user.isEmailVerified()) {
                // Nếu email chưa được xác minh, gửi email xác minh lại cho người dùng
                user.sendEmailVerification()
                        .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Verification email sent, please verify your email.", Toast.LENGTH_LONG).show())
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to send verification email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                // Nếu email đã được xác minh, tiến hành hủy kích hoạt tài khoản
                Toast.makeText(getContext(), "Your email is already verified, proceeding with deactivation.", Toast.LENGTH_SHORT).show();
                deactivateAccount();
            }
        });

        // Xóa tài khoản
        btnDeleteAccount.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null && !user.isEmailVerified()) {
                // Nếu email chưa được xác minh, gửi email xác minh lại cho người dùng
                user.sendEmailVerification()
                        .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Verification email sent, please verify your email to proceed with account deletion.", Toast.LENGTH_LONG).show())
                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to send verification email: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                // Nếu email đã được xác minh, tiến hành xóa tài khoản
                Toast.makeText(getContext(), "Your email is already verified, proceeding with account deletion.", Toast.LENGTH_SHORT).show();
                deleteAccount();
            }
        });

        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            mAuth.signOut(); // Đăng xuất Firebase

            // Mở lại màn hình Login và xóa ngăn xếp backstack
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }

    private void deactivateAccount() {
        // Hàm này sẽ thực hiện các công việc cần thiết để hủy kích hoạt tài khoản,
        // ví dụ như yêu cầu người dùng xác nhận trước khi thực hiện thao tác hủy.
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        imagePickerLauncher.launch(intent);
    }

    private void loadUserProfile() {
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        etDisplayName.setText(documentSnapshot.getString("displayName"));
                        etBio.setText(documentSnapshot.getString("bio"));
                        etContact.setText(documentSnapshot.getString("contact"));
                        ratingBar.setRating(documentSnapshot.getDouble("rating") != null ?
                                Objects.requireNonNull(documentSnapshot.getDouble("rating")).floatValue() : 0f);
                        currentImageUrl = documentSnapshot.getString("profileImageUrl");

                        if (currentImageUrl != null && !currentImageUrl.isEmpty()) {
                            Glide.with(this).load(currentImageUrl).into(imgProfile);
                        } else {
                            imgProfile.setImageResource(R.drawable.ic_user_placeholder);
                        }
                    }
                });
    }

    private void saveUserProfile() {
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        String displayName = etDisplayName.getText().toString().trim();
        String bio = etBio.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        float rating = ratingBar.getRating();

        if (selectedImageUri != null) {
            String extension = getFileExtension(selectedImageUri);
            if (extension == null || extension.isEmpty()) extension = "jpg";
            StorageReference fileRef = storageRef.child(uid + "." + extension);

            fileRef.putFile(selectedImageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        updateFirestoreProfile(uid, displayName, bio, contact, rating, imageUrl);
                    }))
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Lỗi tải ảnh lên, sẽ dùng ảnh cũ", Toast.LENGTH_SHORT).show();
                        Log.e("ProfileFragment", "Upload failed: " + e.getMessage());
                        updateFirestoreProfile(uid, displayName, bio, contact, rating, currentImageUrl);
                    });
        } else {
            updateFirestoreProfile(uid, displayName, bio, contact, rating, currentImageUrl);
        }
    }

    private void updateFirestoreProfile(String uid, String displayName, String bio, String contact, float rating, String imageUrl) {
        db.collection("users").document(uid).update(
                        "displayName", displayName,
                        "bio", bio,
                        "contact", contact,
                        "rating", (double) rating,
                        "profileImageUrl", imageUrl
                ).addOnSuccessListener(unused -> Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi cập nhật", Toast.LENGTH_SHORT).show());
    }

    private String getFileExtension(Uri uri) {
        if (uri == null) return "jpg";
        ContentResolver cr = requireContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = cr.getType(uri);
        return (type != null) ? mime.getExtensionFromMimeType(type) : "jpg";
    }

    // Phương thức để xóa tài khoản người dùng
    private void deleteAccount() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (!user.isEmailVerified()) {
                // Nếu email chưa được xác minh, gửi lại email xác minh
                user.sendEmailVerification()
                        .addOnSuccessListener(aVoid -> {
                            // Thông báo người dùng đã gửi email xác minh
                            Toast.makeText(getContext(), "Verification email sent. Please verify your email before deleting the account.", Toast.LENGTH_LONG).show();
                            // Khóa chức năng xóa tài khoản cho đến khi người dùng xác minh
                            // Có thể gửi một yêu cầu đến người dùng để quay lại sau khi đã xác minh email
                        })
                        .addOnFailureListener(e -> {
                            // Thông báo nếu gửi email thất bại
                            Toast.makeText(getContext(), "Failed to send verification email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                // Nếu email đã được xác minh, hỏi người dùng có chắc chắn muốn xóa tài khoản
                showDeleteConfirmationDialog(user);
            }
        }
    }

    private void showDeleteConfirmationDialog(FirebaseUser user) {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirm Deletion")
                .setMessage("Are you sure you want to delete your account? This action cannot be undone.")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Kiểm tra lại xem email đã được xác minh trước khi xóa
                    if (!user.isEmailVerified()) {
                        Toast.makeText(getContext(), "Please verify your email first before deleting your account.", Toast.LENGTH_LONG).show();
                    } else {
                        deleteAccountConfirmed(user);
                    }
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void deleteAccountConfirmed(FirebaseUser user) {
        // Tiến hành xóa tài khoản
        user.delete()
                .addOnSuccessListener(aVoid -> {
                    // Nếu xóa thành công, đăng xuất và chuyển về màn hình đăng nhập
                    Toast.makeText(getContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    assert getActivity() != null;
                    getActivity().finish();
                })
                .addOnFailureListener(e -> {
                    // Thông báo lỗi khi xóa tài khoản
                    Toast.makeText(getContext(), "Failed to delete account: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
