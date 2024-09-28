package com.example.converse.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.converse.Models.MessagesModel;
import com.example.converse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<MessagesModel> messagesModels;
    Context context;
//    String receiverId;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;

    public ChatAdapter(Context context, ArrayList<MessagesModel> messagesModels) {
        this.context = context;
        this.messagesModels = messagesModels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == SENDER_VIEW_TYPE)
        {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_sample, parent,false);
            return new SenderViewHolder(view);
        }
        else
        {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_sample, parent,false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(messagesModels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TYPE;
        }
        else
        {
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessagesModel messagesModel = messagesModels.get(position);

        if(holder.getClass() == SenderViewHolder.class)
        {
            ((SenderViewHolder) holder).sender_text.setText(messagesModel.getMessage());
        }
        else
        {
            ((ReceiverViewHolder) holder).receiver_text.setText(messagesModel.getMessage());
        }

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                new AlertDialog.Builder(context)
//                        .setTitle("Delete")
//                        .setMessage("Are you sure to delete this message?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                                String senderRoom = FirebaseAuth.getInstance().getUid() + receiverId;
//                                database.getReference().child("child")
//                                        .child(senderRoom)
//                                        .child(messagesModel.getUid())
//                                        .setValue(null);
//                            }
//                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).show();
//
//                return true;
//            }
//        });


    }

    @Override
    public int getItemCount()
    {
        return messagesModels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder
    {

        TextView receiver_text, receiver_time;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiver_text = itemView.findViewById(R.id.receiver_text);
            receiver_time = itemView.findViewById(R.id.receiver_time);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder
    {

        TextView sender_text, sender_time;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            sender_text = itemView.findViewById(R.id.sender_text);
            sender_time = itemView.findViewById(R.id.sender_time);
        }
    }
}
