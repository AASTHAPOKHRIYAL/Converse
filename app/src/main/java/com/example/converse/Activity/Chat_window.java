package com.example.converse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.converse.Adapter.ChatAdapter;
import com.example.converse.Models.MessagesModel;
import com.example.converse.R;
import com.example.converse.databinding.ActivityChatWindowBinding;
//import com.example.whatsapp.Adapter.ChatAdapter;
//import com.example.whatsapp.Models.MessagesModel;
//import com.example.whatsapp.databinding.ActivityChatsWindowBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class Chat_window extends AppCompatActivity {

    ActivityChatWindowBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatWindowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        final String senderId = auth.getUid(); //TAKEN FROM FIREBASE, THE USER WHO LOGGED IN IS THE SENDER
        String receiverId = getIntent().getStringExtra("uid"); // EXTRACTING FROM ADAPTER

        Log.d("Haha", "sender id" + senderId);
        Log.d("Haha", "receiver id" + receiverId);
//        if (receiverId == null || receiverId.isEmpty()) {
//            Log.e("Chat_window", "Receiver ID is null or empty!");
//        } else {
//            Log.d("Chat_window", "Receiver ID: " + receiverId);
//        }

        String username = getIntent().getStringExtra("username");
//        String profilePhoto = getIntent().getStringExtra("profilePhoto");

        binding.username.setText(username);
//        Picasso.get().load(profilePhoto).placeholder(R.drawable.user).into(image); //loading image from firebase

        binding.backArrow.setOnClickListener(v -> {
            Intent intent = new Intent(getApplication(), MainPage.class);
            startActivity(intent);
        });

        final ArrayList<MessagesModel> messagesModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(this, messagesModels, receiverId);

        binding.chatRecyclerView.setAdapter(chatAdapter); //SET ADAPTER TO LayoutInflater

        LinearLayoutManager layoutManager = new LinearLayoutManager(this); //LINEAR LAYOUT MANAGER-SINCE THE CHATS
        layoutManager.setStackFromEnd(true);
        // LAYOUT IS IN THE FORM OF LIST NOT GRID ETC.
        binding.chatRecyclerView.setLayoutManager(layoutManager);


        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;

        Log.d("Haha", "sender room" + senderRoom);
        Log.d("Haha", "receiver room" + receiverRoom);


        database.getReference().child("chats") //SHOWING MESSAGES TO THE SENDER FROM FIREBASE
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messagesModels.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren())
                        {
                            MessagesModel model = snapshot1.getValue(MessagesModel.class);

                            messagesModels.add(model);
                        }

                        binding.chatRecyclerView.scrollToPosition(messagesModels.size() - 1); // Scroll to the bottom
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Chats_window", "Database error: " + error.getMessage());
                    }
                });


        binding.sendButton.setEnabled(true);

        binding.sendButton.setOnClickListener(v -> {

            String message = binding.messageInput.getText().toString();

            if (message.isEmpty()) {
                // Display a toast or a message to the user indicating that the message is empty
                Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
                binding.sendButton.setEnabled(false);
            } else {
                binding.sendButton.setEnabled(true);
                final MessagesModel model = new MessagesModel(senderId, message);
                model.setTimestamp(new Date().getTime());
                binding.messageInput.setText("");

                database.getReference().child("chats") //FIRST TIME CREATING CHATS NODE
                    .child(senderRoom)
                    .push()
                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

//                                String messageId = database.getReference().child("chats").child(senderRoom).push().getKey();
//                                model.setMessageId(messageId); // Set the messageId in the model

                                Log.d("Chat_window", "Message sent to Sender Node: " + senderRoom);
                                database.getReference().child("chats")
                                        .child(receiverRoom)
                                        .push()
                                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Chat_window", "Message sent to Receiver Node: " + receiverRoom);
                                            }
                                        });
                            }
                        });
            }
        }); //SETTING UP CHATS NODE IN FIREBASE AND SENDING MESSAGE ON SEND BUTTON CLICK

//
//        binding.messageInput.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                String message = s.toString().trim();
//                binding.sendButton.setEnabled(!message.isEmpty());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.drop_down_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.view_contact) {
//            Toast.makeText(this, "View contact clicked", Toast.LENGTH_LONG).show();  // Increased duration
////      Intent intent
////      startActivity(intent) = new Intent(getApplicationContext(), ViewContact.class);;
//        } else {
//            Toast.makeText(this, "Something else clicked", Toast.LENGTH_LONG).show();  // Increased duration
//        }
//        Log.d("Chats_window", "Menu item selected: " + item.getTitle());  // Log to confirm the item click is detected
//        return true;
    }

}