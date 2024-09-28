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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {

    ArrayList<Users> contacts;
    Context context;

    public CallAdapter(ArrayList<Users> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_call_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = contacts.get(position);
        holder.usernamee.setText(users.getUserName());
        if(users.getProfilePhoto() != null)
        {
            Picasso.get().
                    load(users.getProfilePhoto()).
                    placeholder(R.drawable.user).
                    into(holder.profile_image);
        }
        else
        {
            holder.profile_image.setImageResource(R.drawable.user);
        }

        holder.time.setText(users.getTime());
    }

    @Override
    public int getItemCount() {
        Log.d("TAG", "NO. OF USERS: "+contacts.size());
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profile_image;
        TextView usernamee, time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile_image = itemView.findViewById(R.id.profile_image);
            usernamee = itemView.findViewById(R.id.user_namee);
            time = itemView.findViewById(R.id.time);
        }
    }
}
