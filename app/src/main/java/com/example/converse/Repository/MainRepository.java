package com.example.converse.Repository;

import android.util.Log;

import com.example.converse.remote.FirebaseClient;
import com.example.converse.utility.DataModel;
import com.example.converse.utility.DataModelType;
import com.example.converse.utility.ErrorCallback;
import com.example.converse.utility.NewEventCallBack;
import com.example.converse.utility.SuccessCallback;
import com.google.firebase.auth.FirebaseAuth;

public class MainRepository {
    private FirebaseClient firebaseClient;
    private String currentUsername;

    private void updateCurrentUsername(String username) {
        this.currentUsername = username;
    }

    private MainRepository() {
        this.firebaseClient = new FirebaseClient();
    }

    private static MainRepository instance;

    public static MainRepository getInstance() {
        if (instance == null) {
            instance = new MainRepository();
        }
        return instance;
    }

    public void login(String username, SuccessCallback callBack) {
        firebaseClient.login(username, () -> {
            updateCurrentUsername(username);  // Keep track of the current username
            Log.d("TAG", "Username: " + username + " found");
            callBack.onSuccess();  // Notify that login was successful
        });
    }

    public void sendCallRequest(String target, ErrorCallback errorCallback, SuccessCallback successCallback) {
        firebaseClient.sendMessageToUser(new DataModel(target, FirebaseAuth.getInstance().getCurrentUser().toString(), null, DataModelType.StartCall), error -> {
            if (error != null) {
                errorCallback.onError(error);  // Call error callback
            } else {
                successCallback.onSuccess();  // Call success callback
            }
        });
    }

    public void subscribeForLatestEvent(NewEventCallBack callBack) {
        firebaseClient.observeIncomingLatestEvent(model -> {
            switch (model.getType()) {
                case Offer:
                    break;
                case Answer:
                    break;
                case IceCandidate:
                    break;
                case StartCall:
                    callBack.OnNewEventReceived(model);
                    break;
            }
        });
    }
}
