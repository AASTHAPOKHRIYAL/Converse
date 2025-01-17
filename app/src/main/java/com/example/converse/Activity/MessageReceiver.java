package com.example.converse.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.converse.Models.MessagesModel;
import com.example.converse.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String messageId = intent.getStringExtra("messageId");
        String messageText = intent.getStringExtra("messageText");
        String receiverId = intent.getStringExtra("receiverId");
        String senderId = intent.getStringExtra("senderId");

        // Reference to the receiver's node
        DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference("chats").child(receiverId + senderId);

        // Create the message object
        MessagesModel model = new MessagesModel(senderId, messageText);
        model.setTimestamp(System.currentTimeMillis());

        // Save the message in the receiver's node
        if (messageId != null) {
            Log.d("messageReceiver", "Reached the start...goin for receiver database");
            receiverRef.child(messageId).setValue(model)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("MessageReceiver", "Message delivered successfully to receiver");
                        } else {
                            Log.e("MessageReceiver", "Failed to deliver message to receiver");
                        }
                    });
        }
        Log.d("messageReceiver", "Successful");
    }
}