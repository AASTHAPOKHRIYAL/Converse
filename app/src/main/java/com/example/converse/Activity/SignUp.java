package com.example.converse.Activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.converse.Fragment.BottomSheetFragment;
import com.example.converse.Fragment.LoginFragment;
import com.example.converse.databinding.ActivitySignUpBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SignUp extends AppCompatActivity {

    Button button2;
    TextView loginText;
    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initialize views using binding
        button2 = binding.button2;
        loginText = binding.loginText;

        button2.setOnClickListener(v -> {
            BottomSheetDialogFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getSupportFragmentManager(), "BottomSheetFragmentTag");
        });

        loginText.setOnClickListener(v -> {
            BottomSheetDialogFragment loginFragment = new LoginFragment();
            loginFragment.show(getSupportFragmentManager(), "LoginFragment1Tag");
        });
    }
}