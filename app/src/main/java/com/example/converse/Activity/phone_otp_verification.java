package com.example.converse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.converse.databinding.ActivityPhoneOtpVerificationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class phone_otp_verification extends AppCompatActivity {

    public ActivityPhoneOtpVerificationBinding binding;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPhoneOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textView12.setVisibility(View.INVISIBLE);
        binding.textView13.setVisibility(View.INVISIBLE);
        binding.textView14.setVisibility(View.INVISIBLE);
        binding.textView15.setVisibility(View.INVISIBLE);

        jumpWhileEnteringValues();
        binding.progressBar2.setVisibility(View.GONE);

        binding.phoneNo.setText(String.format(
                "+91-%s", getIntent().getStringExtra("phone")
        ));
        verificationId = getIntent().getStringExtra("verificationId");

        binding.resendBtn.setOnClickListener(v -> Toast.makeText(phone_otp_verification.this, "OTP resent successfully", Toast.LENGTH_SHORT).show());

        binding.verifyBtn.setOnClickListener(v -> {
            binding.progressBar2.setVisibility(View.VISIBLE);
            binding.verifyBtn.setVisibility(View.INVISIBLE);
            if (binding.et1.getText().toString().isEmpty() || binding.et2.getText().toString().isEmpty() || binding.et3.getText().toString().isEmpty() || binding.et4.getText().toString().isEmpty() || binding.et5.getText().toString().isEmpty() || binding.et6.getText().toString().isEmpty()) {
                Toast.makeText(phone_otp_verification.this, "OTP cannot be empty", Toast.LENGTH_SHORT).show();
                binding.verifyBtn.setVisibility(View.VISIBLE);
                binding.progressBar2.setVisibility(View.INVISIBLE);
            } else {
                if (verificationId != null) {
                    String code = binding.et1.getText().toString() +
                            binding.et2.getText().toString() +
                            binding.et3.getText().toString() +
                            binding.et4.getText().toString() +
                            binding.et5.getText().toString() +
                            binding.et6.getText().toString();

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
                    FirebaseAuth.getInstance()
                            .signInWithCredential(credential).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    binding.progressBar2.setVisibility(View.VISIBLE);
                                    binding.verifyBtn.setVisibility(View.INVISIBLE);
                                    Toast.makeText(phone_otp_verification.this, "OTP matched", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), Account_created_popup_activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else {
                                    binding.progressBar2.setVisibility(View.GONE);
                                    binding.verifyBtn.setVisibility(View.VISIBLE);

                                    binding.textView12.setVisibility(View.VISIBLE);
                                    binding.textView13.setVisibility(View.VISIBLE);
                                    binding.textView14.setVisibility(View.VISIBLE);
                                    binding.textView15.setVisibility(View.VISIBLE);

                                    binding.textView13.setOnClickListener(v1 -> {
                                        Intent intent = new Intent(getApplicationContext(), SignUp.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    });

                                    binding.textView15.setOnClickListener(v1 -> {
                                        Intent intent = new Intent(getApplicationContext(), SignUp.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    });
                                    Toast.makeText(phone_otp_verification.this, "OTP not valid", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }

    private void jumpWhileEnteringValues() //WHEN NUMBER IS ENTERED IN 1ST ONE IT SHOULD AUTOMATICALLY JUMP TO THE NEXT ONE.
    {
        binding.et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && binding.et2.getText().toString() != null)
                    binding.et2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && binding.et3.getText().toString() != null)
                    binding.et3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && binding.et4.getText().toString() != null)
                    binding.et4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && binding.et5.getText().toString() != null)
                    binding.et5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.et5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && binding.et6.getText().toString() != null)
                    binding.et6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}