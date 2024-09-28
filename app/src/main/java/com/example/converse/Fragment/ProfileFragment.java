package com.example.converse.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.converse.Activity.Profile_Picture_Subactivity;
import com.example.converse.Models.Users;
import com.example.converse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileFragment extends Fragment {
    LinearLayout language_layout, privacy_layout, devices_layout, notifications_layout, my_account_layout, profile_picture_layout;
    ImageView image;
    FirebaseDatabase database;
    TextView about, profile_name;
    Users users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = FirebaseDatabase.getInstance();

        language_layout = view.findViewById(R.id.language_layout);
        privacy_layout = view.findViewById(R.id.privacy_layout);
        devices_layout = view.findViewById(R.id.devices_layout);
        notifications_layout = view.findViewById(R.id.notifications_layout);
        my_account_layout = view.findViewById(R.id.my_account_layout);
        profile_picture_layout = view.findViewById(R.id.profile_picture_layout);

        image = view.findViewById(R.id.image);
        about = view.findViewById(R.id.about);
        profile_name = view.findViewById(R.id.profile_name);


        database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Users users = snapshot.getValue(Users.class);

                        if (users != null) {
                            Picasso.get().load(users.getProfilePhoto())
                                    .placeholder(R.drawable.user)
                                    .into(image);

                            profile_name.setText(users.getUserName());
                            about.setText(users.getAbout());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        profile_picture_layout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Profile_Picture_Subactivity.class);
            startActivityForResult(intent, 33);
        });
    }

//    @Override
//    public void onBackPressed() {
//        ChatsFragment chatsFragment = new ChatsFragment();
//        requireActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frame_layout, chatsFragment)
//                .commit();
//    }
}