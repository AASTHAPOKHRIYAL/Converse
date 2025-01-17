package com.example.converse.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.converse.Models.MessagesModel;
import com.example.converse.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class MessageSchedulerActivity extends AppCompatActivity {

    private ImageButton datePickerIcon, timePickerIcon;
    private DatabaseReference database;
    private String senderRoom, senderId, receiverId;
    private EditText messageInput;
    private Button scheduleButton;
    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_scheduler);

        // Initialize views
        datePickerIcon = findViewById(R.id.date_picker_icon);
        timePickerIcon = findViewById(R.id.time_picker_icon);
        messageInput = findViewById(R.id.message_input);
        scheduleButton = findViewById(R.id.schedule_button);

        // Initialize Firebase and variables
        database = FirebaseDatabase.getInstance().getReference();
        senderId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        receiverId = getIntent().getStringExtra("receiverId");
        senderRoom = senderId + receiverId;
        selectedDateTime = Calendar.getInstance();

        // Set up date picker
        datePickerIcon.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            MaterialDatePicker<Long> datePicker = builder.build();
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");

            datePicker.addOnPositiveButtonClickListener(selection -> {
                selectedDateTime.setTimeInMillis(selection);
                selectedDateTime.set(Calendar.HOUR_OF_DAY, 0);
                selectedDateTime.set(Calendar.MINUTE, 0);
                selectedDateTime.set(Calendar.SECOND, 0);
                Toast.makeText(this, "Date selected: " + datePicker.getHeaderText(), Toast.LENGTH_SHORT).show();
            });
        });

        // Set up time picker
        timePickerIcon.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, hourOfDay, minute) -> {
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);
                        selectedDateTime.set(Calendar.SECOND, 0);
                        Toast.makeText(this, "Time selected: " + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
                    },
                    selectedDateTime.get(Calendar.HOUR_OF_DAY),
                    selectedDateTime.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.show();
        });

        // Set up schedule button
        scheduleButton.setOnClickListener(v -> scheduleMessage());
    }

    private void scheduleMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (messageText.isEmpty()) {
            Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Ensure the selected time is in the future
        if (selectedDateTime.before(Calendar.getInstance())) {
            Toast.makeText(this, "Scheduled time must be in the future", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a unique message ID
        String messageId = database.child("chats").child(senderRoom).push().getKey();
        if (messageId == null) {
            Toast.makeText(this, "Failed to generate message ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and save the message
        MessagesModel message = new MessagesModel(senderId, messageText);
        message.setTimestamp(selectedDateTime.getTimeInMillis());

        database.child("chats").child(senderRoom).child(messageId).setValue(message)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Message scheduled successfully", Toast.LENGTH_SHORT).show();
                    scheduleMessageDelivery(messageId, message);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to schedule message", Toast.LENGTH_SHORT).show();
                    Log.e("MessageScheduler", "Error scheduling message", e);
                });
    }

    private void scheduleMessageDelivery(String messageId, MessagesModel message) {
        Intent intent = new Intent(this, MessageReceiver.class);
        intent.putExtra("messageId", messageId);
        intent.putExtra("messageText", message.getMessage());
        intent.putExtra("receiverId", receiverId);
        intent.putExtra("senderId", senderId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, message.getTimestamp(), pendingIntent);
        } else {
            Toast.makeText(this, "Error setting up AlarmManager", Toast.LENGTH_SHORT).show();
        }

        Log.d("MessageScheduleActivity", "Reached the end...goin for messageReceiver");
        finish();
    }

}
