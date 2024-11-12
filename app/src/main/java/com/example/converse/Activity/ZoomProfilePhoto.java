// ZoomProfilePhoto.java
package com.example.converse.Activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.converse.R;
import com.squareup.picasso.Picasso;

public class ZoomProfilePhoto extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_profile_photo);

        // Retrieve data from the Intent
        String username = getIntent().getStringExtra("username");
        String profilePhotoUrl = getIntent().getStringExtra("profilePhoto");

        // Initialize views
        TextView usernameTextView = findViewById(R.id.username);
        ImageView profileImageView = findViewById(R.id.profile_image);

        // Set data to views
        usernameTextView.setText(username);
        Picasso.get().load(profilePhotoUrl).placeholder(R.drawable.user).into(profileImageView);
    }
}
