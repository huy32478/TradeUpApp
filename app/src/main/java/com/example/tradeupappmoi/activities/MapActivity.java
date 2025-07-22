package com.example.tradeupappmoi.activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.tradeupappmoi.R;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);  // Gọi callback khi map sẵn sàng.
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // Tạo một marker tại một vị trí cố định, ví dụ: Sydney.
        LatLng sydney = new LatLng(-34.0, 151.0);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        // Di chuyển camera tới vị trí đó và zoom vào.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f));
    }
}