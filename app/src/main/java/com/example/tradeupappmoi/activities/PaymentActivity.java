package com.example.tradeupappmoi.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tradeupappmoi.R;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.Stripe;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.ApiResultCallback;

public class PaymentActivity extends AppCompatActivity {

    private Stripe stripe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardInputWidget cardInputWidget = findViewById(R.id.cardInputWidget);
        Button payButton = findViewById(R.id.payButton);

        stripe = new Stripe(
                getApplicationContext(),
                PaymentConfiguration.getInstance(getApplicationContext()).getPublishableKey()
        );

        payButton.setOnClickListener(v -> {
            PaymentMethodCreateParams cardParams = cardInputWidget.getPaymentMethodCreateParams();
            if (cardParams != null) {
                stripe.createPaymentMethod(
                        cardParams,
                        new ApiResultCallback<>() {
                            @Override
                            public void onSuccess(@NonNull PaymentMethod paymentMethod) {
                                Toast.makeText(
                                        PaymentActivity.this,
                                        "Thanh toán test thành công!\nID: " + paymentMethod.id,
                                        Toast.LENGTH_LONG
                                ).show();
                            }

                            @Override
                            public void onError(@NonNull Exception e) {
                                Toast.makeText(
                                        PaymentActivity.this,
                                        "Lỗi: " + e.getLocalizedMessage(),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                        }
                );
            } else {
                Toast.makeText(PaymentActivity.this, "Vui lòng nhập thông tin thẻ hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}


