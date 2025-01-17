package com.example.converse.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.converse.Models.Users;
import com.example.converse.R;
import com.example.converse.databinding.ActivityProfilePictureSubactivityBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class Profile_Picture_Subactivity extends AppCompatActivity {
    ActivityProfilePictureSubactivityBinding binding;
    FirebaseStorage storage;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfilePictureSubactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.progressBar.setVisibility(View.GONE);

        // BACK BUTTON
        binding.backArrow.setOnClickListener(v -> finish());

        // Load user data from Firebase
        database.getReference().child("Users").child(Objects.requireNonNull(auth.getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if (users != null) {
                            Picasso.get().load(users.getProfilePhoto())
                                    .placeholder(R.drawable.user)
                                    .into(binding.image);

                            binding.username.setText(users.getUserName());
                            binding.about.setText(users.getAbout());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error if necessary
                    }
                });

        // Update user data
        binding.updateButton.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);

            String updatedUserName = binding.username.getText().toString();
            String updatedAbout = binding.about.getText().toString();

            database.getReference().child("Users")
                    .child(auth.getUid())
                    .child("userName")
                    .setValue(updatedUserName)
                    .addOnSuccessListener(unused -> {
                        database.getReference().child("Users")
                                .child(auth.getUid())
                                .child("about")
                                .setValue(updatedAbout)
                                .addOnSuccessListener(unused1 -> binding.progressBar.setVisibility(View.GONE))
                                .addOnFailureListener(e -> binding.progressBar.setVisibility(View.GONE));
                    })
                    .addOnFailureListener(e -> binding.progressBar.setVisibility(View.GONE));
        });

        // Open gallery
        binding.plus.setOnClickListener(v -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, 33);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri file = data.getData();
            binding.image.setImageURI(file);
            binding.progressBar.setVisibility(View.VISIBLE);

            final StorageReference reference = storage.getReference().child("profilePhoto")
                    .child(Objects.requireNonNull(auth.getUid()));
            reference.putFile(file)
                    .addOnSuccessListener(taskSnapshot -> reference.getDownloadUrl().addOnSuccessListener(uri -> {
                        database.getReference().child("Users")
                                .child(auth.getUid())
                                .child("profilePhoto")
                                .setValue(uri.toString())
                                .addOnSuccessListener(unused -> binding.progressBar.setVisibility(View.GONE))
                                .addOnFailureListener(e -> binding.progressBar.setVisibility(View.GONE));
                    }).addOnFailureListener(e -> binding.progressBar.setVisibility(View.GONE)))
                    .addOnFailureListener(e -> binding.progressBar.setVisibility(View.GONE));
        } else {
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}