package com.example.converse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.converse.Activity.Chat_window;
import com.example.converse.Activity.ZoomProfilePhoto;
import com.example.converse.Models.Users;
import com.example.converse.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    ArrayList<Users> list;
    Context context;

    public UsersAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("TAG", "Reached in show user: ");
        View view = LayoutInflater.from(context).inflate(R.layout.show_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = list.get(position); //extracting position
        if (users.getProfilePhoto() != null && !users.getProfilePhoto().isEmpty()) {
            Picasso.get()
                    .load(users.getProfilePhoto())  // Load the image from the URL
                    .placeholder(R.drawable.user)  // Default placeholder
                    .into(holder.image);  // Into the target ImageView
        } else {
            holder.image.setImageResource(R.drawable.user);  // Set default image if no URL
        }
        holder.user_name.setText(users.getUserName()); //loading name from firebase

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Chat_window.class);
                intent.putExtra("uid", users.getUserId());
                intent.putExtra("username", users.getUserName());
                intent.putExtra("profilePhoto", users.getProfilePhoto());

                Log.d("Haha", "sender id adapter" + users.getUserId());

                context.startActivity(intent);
            }
        });

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ZoomProfilePhoto.class);
                intent.putExtra("username", users.getUserName());
                intent.putExtra("profilePhoto", users.getProfilePhoto());
                context.startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance().getUid()+ users.getUserId()).orderByChild("timestamp").limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChildren())
                {
                    for(DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        holder.last_message.setText(Objects.requireNonNull(snapshot1.child("message").getValue()).toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // Method to filter the list based on the search query
//    public void filterList(String query) {
//        if (query.isEmpty()) {
//            list = new ArrayList<>(listFull); // Reset to full list if query is empty
//        } else {
//            ArrayList<Users> filteredList = new ArrayList<>();
//            for (Users user : listFull) {
//                if (user.getUserName().toLowerCase().contains(query.toLowerCase())) {
//                    filteredList.add(user);
//                }
//            }
//            list = filteredList;
//        }
//        notifyDataSetChanged();
//    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "NO. OF USERS: "+list.size());
        return list.size(); //returning no of users
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView image;
        TextView user_name, last_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profile_image);
            user_name = itemView.findViewById(R.id.username);
            last_message = itemView.findViewById(R.id.last_message);
        }
    }
}
