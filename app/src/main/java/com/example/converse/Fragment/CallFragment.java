package com.example.converse.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.converse.Activity.CallSubactivity;
import com.example.converse.Adapter.CallAdapter;
import com.example.converse.Models.Users;
import com.example.converse.Repository.MainRepository;
import com.example.converse.databinding.FragmentCallBinding;
import com.example.converse.utility.DataModelType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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
    MainRepository mainRepository;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentCallBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        database = FirebaseDatabase.getInstance();
        Log.d("TAG", "we are in the call fragment");

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

        binding.addCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CallSubactivity.class);
                startActivity(intent);
            }
        });

//        binding.searchContact.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }

//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(binding.searchContact.getText().toString().equals(""))
//                {
//                    Toast.makeText(getContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    str = s.toString();
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

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



//    void askPermissions()
//    {
//        ActivityCompat.requestPermissions(getActivity(), permissions, requestCode);
//    }
//
//    private boolean permissionGranted(){
//        for(String permission: permissions){
//            if(ActivityCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED)
//            {
//                return false;
//            }
//        }
//
//        return true;
//    }

}