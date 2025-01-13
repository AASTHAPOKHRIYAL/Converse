package com.example.converse.Activity;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.converse.Models.Users;
import com.example.converse.R;
import com.example.converse.Repository.MainRepository;
import com.example.converse.databinding.ActivityCallSubactivityBinding;
import com.example.converse.utility.DataModelType;

public class CallSubactivity extends AppCompatActivity {
    ActivityCallSubactivityBinding binding;
    MainRepository mainRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_subactivity);

        // Retrieve the username from the intent
//        String userName = getIntent().getStringExtra("username");

        // Display the caller's name on the UI
//        binding.incomingNameTV.setText(userName + " is calling you");

        // Set up accept and reject button logic
//        setupCallHandling(userName);
    }


//    private void setupCallHandling(String userName) {
//        mainRepository = MainRepository.getInstance();
//
//        binding.acceptButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Start call request or handle call acceptance logic
//                Toast.makeText(CallSubactivity.this, "Call accepted with " + userName, Toast.LENGTH_SHORT).show();
//                binding.incomingCallLayout.setVisibility(View.GONE);
//                // Additional code to handle accepting the call can be placed here
//            }
//        });
//
//        binding.rejectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Handle call rejection
//                Toast.makeText(CallSubactivity.this, "Call rejected from " + userName, Toast.LENGTH_SHORT).show();
//                binding.incomingCallLayout.setVisibility(View.GONE);
//            }
//        });
//    }
}