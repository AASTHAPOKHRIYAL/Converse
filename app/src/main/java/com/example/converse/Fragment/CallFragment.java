package com.example.converse.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.converse.Adapter.CallAdapter;
import com.example.converse.Models.Users;
import com.example.converse.databinding.FragmentCallBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CallFragment extends Fragment {
    FragmentCallBinding binding;
    CallAdapter callAdapter;
    String [] permissions= new String[] {Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO};
    private int requestCode = 1;
    ArrayList<Users> contacts = new ArrayList<>();
    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentCallBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        database = FirebaseDatabase.getInstance();

        callAdapter = new CallAdapter(contacts, getContext());
        binding.recyclerView.setAdapter(callAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerView.setLayoutManager(layoutManager);

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               contacts.clear();

                Log.d("TAG", "Current User ID: ");

               for(DataSnapshot snapshot1: snapshot.getChildren())
               {
                   Users users = snapshot1.getValue(Users.class);

                   users.getUserId(snapshot1.getKey());
                   contacts.add(users);
                   Log.d("TAG", "Data fetched again: ");
               }

               callAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Cannot fetch contact data");
            }
        });


//        button.setOnClickListener()
//        {
//            if (permissionGranted()) {
//                Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
//            } else {
//                askPermissions();
//            }
//        }

        return view;
    }

    void askPermissions()
    {
        ActivityCompat.requestPermissions(getActivity(), permissions, requestCode);
    }

    private boolean permissionGranted(){
        for(String permission: permissions){
            if(ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }

        return true;
    }
}