package com.example.tradeupappmoi;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.tradeupappmoi.fragments.AddItemFragment;
import com.example.tradeupappmoi.fragments.HomeFragment;
import com.example.tradeupappmoi.fragments.MessagesFragment;
import com.example.tradeupappmoi.fragments.ProfileFragment;
import com.example.tradeupappmoi.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @SuppressLint({"MissingInflatedId", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Đảm bảo layout lùi ra cho notch, camera,...
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // === Xử lý BottomNavigationView ===
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load mặc định là HomeFragment khi mở app
        loadFragment(new HomeFragment());

        // Xử lý khi chọn item trong bottom navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment;

            int id = item.getItemId();
            if (id == R.id.nav_search) {
                selectedFragment = new SearchFragment();
            } else if (id == R.id.nav_add) {
                selectedFragment = new AddItemFragment();
            } else if (id == R.id.nav_messages) {
                selectedFragment = new MessagesFragment();
            } else if (id == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            } else {
                selectedFragment = new HomeFragment();
            }

            loadFragment(selectedFragment);
            return true;
        });

    }

    // Hàm để thay Fragment
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, fragment)
                .commit();
    }
}
