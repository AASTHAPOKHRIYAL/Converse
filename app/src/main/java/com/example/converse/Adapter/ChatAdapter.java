package com.example.converse.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.converse.Models.MessagesModel;
import com.example.converse.Models.Users;
import com.example.converse.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//ADAPTER USED FOR BINDING BACKEND DATA TO THE UI->SHOWING DATA IN FRONTEND LIKE MESSAGE, DATE, TIME, PROFILE PHOTO, USERNAME, ETC.
public class ChatAdapter extends RecyclerView.Adapter {

    ArrayList<MessagesModel> messagesModels;
    Context context;
    Users users;
    //    String receiverId;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;
    String receiverId;

    public ChatAdapter(Context context, ArrayList<MessagesModel> messagesModels, String receiverId) {
        this.context = context;
        this.messagesModels = messagesModels;
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == SENDER_VIEW_TYPE) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_sample, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_sample, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (messagesModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid())) {
            return SENDER_VIEW_TYPE;
        } else {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessagesModel messagesModel = messagesModels.get(position);

        Log.d("ChatAdapter", "Message: " + messagesModel.getMessage() + ", ID: " + messagesModel.getMessageId());

        String formattedTime = "";
        if (messagesModel.getTimestamp() > 0) { // Ensure timestamp exists
            Date date = new Date(messagesModel.getTimestamp());
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            formattedTime = formatter.format(date);
        }

        if (holder.getClass() == SenderViewHolder.class) {
            ((SenderViewHolder) holder).sender_text.setText(messagesModel.getMessage());
            ((SenderViewHolder) holder).sender_time.setText(formattedTime);

//            Picasso.get().load(messagesModel.getSenderURL()).placeholder(R.drawable.user).into(((SenderViewHolder) holder).sender_image);

            fetchUserProfile(messagesModel.getUid(), url -> {
                Picasso.get().load(url)
                        .placeholder(R.drawable.user) // Default placeholder
                        .into(((SenderViewHolder) holder).sender_image);
            });

        } else {
            ((ReceiverViewHolder) holder).receiver_text.setText(messagesModel.getMessage());
            ((ReceiverViewHolder) holder).receiver_time.setText(formattedTime);

//            Picasso.get().load(messagesModel.getReceiverURL()).placeholder(R.drawable.user).into(((ReceiverViewHolder) holder).receiver_image);

            fetchUserProfile(receiverId, url -> {
                Picasso.get().load(url)
                        .placeholder(R.drawable.user) // Default placeholder
                        .into(((ReceiverViewHolder) holder).receiver_image);
            });

        }

        //DELETING A MESSAGE ON LONG PRESS
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
                                Log.d("Hmm", "MessageID: " + messagesModel.getMessageId());

                                Log.d("Hmm", "UID: " + senderRoom);

                                if (messagesModel.getMessageId() != null) {
                                    database.getReference().child("chats")
                                            .child(senderRoom)
                                            .child(messagesModel.getMessageId())
                                            .setValue(null);
                                } else {
                                    Log.e("ChatAdapter", "Message ID is null, cannot delete message");
                                }
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return messagesModels.size();
    }

    private void fetchUserProfile(String userId, OnSuccessListener<String> onSuccessListener) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Users user = snapshot.getValue(Users.class);
                        if (user != null && user.getProfilePhoto() != null) {
                            onSuccessListener.onSuccess(user.getProfilePhoto());
                        } else {
                            Log.e("ChatAdapter", "Profile photo not found for user " + userId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("ChatAdapter", "Failed to fetch user data for user " + userId, error.toException());
                    }
                });
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiver_text, receiver_time;
        ImageView receiver_image;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiver_text = itemView.findViewById(R.id.receiver_text);
            receiver_time = itemView.findViewById(R.id.receiver_time);
            receiver_image = itemView.findViewById(R.id.receiver_image);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {

        TextView sender_text, sender_time;
        ImageView sender_image;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            sender_text = itemView.findViewById(R.id.sender_text);
            sender_time = itemView.findViewById(R.id.sender_time);
            sender_image = itemView.findViewById(R.id.sender_image);
        }
    }
}
