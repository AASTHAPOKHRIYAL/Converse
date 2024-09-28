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

        //BACK BUTTON
        binding.backArrow.setOnClickListener(v -> finish());

        database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
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

                    }
                });

        binding.updateButton.setOnClickListener(v -> {

            binding.progressBar.setVisibility(View.VISIBLE);

            database.getReference().child("Users")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child("userName")
                    .setValue(binding.username.getText().toString());

            database.getReference().child("Users")
                    .child(FirebaseAuth.getInstance().getUid())
                    .child("about")
                    .setValue(binding.about.getText().toString());

            binding.progressBar.setVisibility(View.GONE);

        });

        binding.plus.setOnClickListener(v -> { //FOR OPENING GALLARY
            Intent galaryIntent = new Intent();
            galaryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galaryIntent.setType("image/*");
            startActivityForResult(galaryIntent, 33);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) { //TO SET THE IMAGE
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getData() != null) {
            Uri file = data.getData();
            binding.image.setImageURI(file);

            binding.progressBar.setVisibility(View.VISIBLE);

            final StorageReference reference = storage.getReference().child("profilePhoto")
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
            reference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(auth.getUid()).child("profilePhoto").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }
}