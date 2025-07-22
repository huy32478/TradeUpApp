package com.example.tradeupappmoi;

import android.app.Application;

import com.stripe.android.PaymentConfiguration;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51RgcxhRfUGnxeRx1RwTIlzyiO9snMSrDsAjevA54KCwqcXZ3pi5XCOGUBKb8pJ3aWlGii9XVXD6vEHysKHudSqZM00G6ecqEYn" // Thay bằng publishable key (test mode)
        );
    }
}
