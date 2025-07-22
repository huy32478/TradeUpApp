package com.example.tradeupappmoi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tradeupappmoi.MainActivity;
import com.example.tradeupappmoi.R;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private final ActivityResultLauncher<IntentSenderRequest> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();
                        if (idToken != null) {
                            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                            mAuth.signInWithCredential(firebaseCredential)
                                    .addOnCompleteListener(this, task -> {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user != null && user.isEmailVerified()) {
                                                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(this, MainActivity.class));
                                                finish();
                                            } else {
                                                mAuth.signOut();
                                                Toast.makeText(this, "Please verify your email before logging in", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Google Sign-In error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegisterLink);
        TextView tvForgot = findViewById(R.id.tvForgot);
        TextView tvResendVerification = findViewById(R.id.tvResendVerification);
        com.google.android.gms.common.SignInButton btnGoogle = findViewById(R.id.btnGoogleSignIn);

        mAuth = FirebaseAuth.getInstance();
        oneTapClient = Identity.getSignInClient(this);

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .setAutoSelectEnabled(false)
                .build();

        validateInputs();

        btnLogin.setOnClickListener(v -> loginWithEmail());
        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        tvForgot.setOnClickListener(v -> startActivity(new Intent(this, ForgotPasswordActivity.class)));
        btnGoogle.setOnClickListener(v -> signInWithGoogle());

        // ✅ Resend verification email
        tvResendVerification.setOnClickListener(v -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null && !user.isEmailVerified()) {
                user.sendEmailVerification()
                        .addOnSuccessListener(unused -> Toast.makeText(this, "Verification email resent", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to resend email", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "No user or email already verified", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateInputs() {
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = etEmail.getText().toString().trim();
                String pass = etPassword.getText().toString();
                boolean isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length() >= 6;
                btnLogin.setEnabled(isValid);
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        etEmail.addTextChangedListener(watcher);
        etPassword.addTextChangedListener(watcher);
    }

    private void loginWithEmail() {
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString();

        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                        } else {
                            mAuth.signOut();
                            Toast.makeText(this, "Please verify your email before logging in", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "Login failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    IntentSenderRequest request =
                            new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                    googleSignInLauncher.launch(request);
                })
                .addOnFailureListener(this, e -> Toast.makeText(this, "Google Sign-In failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}