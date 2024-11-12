package com.example.converse.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.converse.utility.DataModel;
import com.example.converse.utility.ErrorCallback;
import com.example.converse.utility.NewEventCallBack;
import com.example.converse.utility.SuccessCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Objects;

public class FirebaseClient {
    private final Gson gson = new Gson();
    private final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    private String currName;
    private static final String LATEST_EVENT_FIELD_NAME = "latest_event";
    FirebaseAuth auth;

    public void login(String username, SuccessCallback callBack) {
        ref.child("Calls").child(username).setValue("") // Create the user's node in "Calls"
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currName = username;  // Set the current username for reference
                        Log.d("TAG", "Username: " + currName + " successfully registered in Calls node");
                        callBack.onSuccess();  // Invoke success callback
                    } else {
                        Log.e("TAG", "Failed to register username in Calls node", task.getException());
                    }
                });
    }


    public void sendMessageToUser(DataModel dataModel, ErrorCallback errorCallback)
    {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(dataModel.getTarget()).exists())
                {
                    //send signal
                    ref.child(dataModel.getTarget()).child(LATEST_EVENT_FIELD_NAME).setValue(gson.toJson(dataModel));
                }
                else
                {
                    errorCallback.onError("Error found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                errorCallback.onError("Error found");
            }
        });
    }

    public void observeIncomingLatestEvent(NewEventCallBack callBack)
    {
        Log.d("TAG", "Username: "+ currName + "received");
        ref.child(currName).child(LATEST_EVENT_FIELD_NAME).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                    String data = Objects.requireNonNull(snapshot.getValue()).toString();
                    DataModel dataModel = gson.fromJson(data, DataModel.class);
                    callBack.OnNewEventReceived(dataModel);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
