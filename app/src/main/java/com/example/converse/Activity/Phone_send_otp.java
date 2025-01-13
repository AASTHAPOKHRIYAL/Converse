package com.example.converse.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.converse.databinding.ActivityPhoneSendOtpBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Phone_send_otp extends AppCompatActivity {

    public ActivityPhoneSendOtpBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneSendOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.textView13.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);
        });
        binding.textView15.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUp.class);
            startActivity(intent);
        });

        binding.sendButton.setOnClickListener(v -> {
            if (binding.editPhoneNo.getText().toString().trim().isEmpty()) {
                Toast.makeText(Phone_send_otp.this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
            } else if (binding.editPhoneNo.getText().toString().trim().length() == 10) {
                sendOTP();
            } else {
                Toast.makeText(Phone_send_otp.this, "Invalid Phone number", Toast.LENGTH_SHORT).show();
            }

        });

    }

    public void sendOTP() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.sendButton.setVisibility(View.INVISIBLE);
        PhoneAuthProvider.OnVerificationStateChangedCallbacks callback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d("OTP", "onVerificationCompleted: " + credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                binding.progressBar.setVisibility(View.GONE);
                binding.sendButton.setVisibility(View.VISIBLE);
                Toast.makeText(Phone_send_otp.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.e("OTP", "onVerificationFailed: ", e);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                binding.progressBar.setVisibility(View.GONE);
                binding.sendButton.setVisibility(View.VISIBLE);
                Toast.makeText(Phone_send_otp.this, "An OTP has been sent to your device", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), phone_otp_verification.class);
                intent.putExtra("phone", binding.editPhoneNo.getText().toString().trim());
                intent.putExtra("verificationId", verificationId);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                Log.d("OTP", "onCodeSent: " + verificationId);
            }
        };

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+1" + binding.editPhoneNo.getText().toString().trim())
                .setTimeout(60L, TimeUnit.SECONDS) //60 sec timeout
                .setActivity(this)
                .setCallbacks(callback)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
}