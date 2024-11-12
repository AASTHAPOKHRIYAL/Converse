package com.example.converse.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.converse.Models.Users;
import com.example.converse.R;
import com.example.converse.Repository.MainRepository;
import com.example.converse.utility.ErrorCallback;
import com.example.converse.utility.SuccessCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallViewHolder> {
    private ArrayList<Users> usernames;  // Assume this is a list of usernames
    private MainRepository mainRepository;
    Context context;
    public CallAdapter(ArrayList<Users> usernames, Context context) {
        this.usernames = usernames;
        this.mainRepository = MainRepository.getInstance();  // Get instance of MainRepository
        this.context = context;
    }

    @NonNull
    @Override
    public CallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_call_user, parent, false);
        return new CallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallViewHolder holder, int position) {
        Users user = usernames.get(position);  // Get the Users object
        String username = user.getUserName();  // Assuming getUsername() returns the username as a String
        holder.usernameTextView.setText(username);

        if(user.getProfilePhoto()!=null)
        {
            Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.user).into(holder.profileImage);
        }
        else
        {
            holder.profileImage.setImageResource(R.drawable.user);
        }

        holder.callBtn.setOnClickListener(v -> {
            // Handle click to start call
            startCall(username);
        });
    }

    private void startCall(String username) {
        mainRepository.sendCallRequest(username, new ErrorCallback() {
            @Override
            public void onError(String error) {
                Log.e("CallAdapter", "Failed to send call request: " + error);
            }
        }, new SuccessCallback() {
            @Override
            public void onSuccess() {
                Log.d("CallAdapter", "Call request sent successfully to " + username);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usernames.size();
    }

    static class CallViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        ImageView callBtn;
        ImageView profileImage;

        public CallViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.user_namee);  // Update with actual ID
            callBtn = itemView.findViewById(R.id.callBtn);
            profileImage = itemView.findViewById(R.id.profile_image);
        }
    }
}
