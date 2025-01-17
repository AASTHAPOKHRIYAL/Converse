package com.example.converse.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.converse.Activity.Account_created_popup_activity;
import com.example.converse.Activity.MainPage;
import com.example.converse.Activity.Phone_send_otp;
import com.example.converse.Models.Users;
import com.example.converse.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    Button signUpButton;
    TextInputEditText usernameText, emailText, passwordText;
    TextView textView5;
    FirebaseDatabase database;
    ProgressDialog progressDialog;
    //GOOGLE SIGN UP
    ImageButton google;
    GoogleSignInClient googleSignInClient;
    int RC_SIGN_IN = 25;
    //PHONE SIGN UP
    ImageButton call;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signUpButton = view.findViewById(R.id.signUpButton);
        emailText = view.findViewById(R.id.emailText);
        usernameText = view.findViewById(R.id.usernameText);
        passwordText = view.findViewById(R.id.passwordText);
        textView5 = view.findViewById(R.id.textView5);
        google = view.findViewById(R.id.google);
        call = view.findViewById(R.id.call);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignUp();
            }
        });

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Creating account");
        progressDialog.setMessage("Please wait...while we create your account");

        textView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment loginFragment = new LoginFragment();
                loginFragment.show(getParentFragmentManager(), loginFragment.getTag());
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(emailText.getText()).toString().trim();
                String password = Objects.requireNonNull(passwordText.getText()).toString().trim();

                if (email.isEmpty()) {
                    emailText.setError("Email is required");
                    emailText.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    passwordText.setError("Password is required");
                    passwordText.requestFocus();
                    return;
                }
                progressDialog.show();
                auth.createUserWithEmailAndPassword(Objects.requireNonNull(emailText.getText()).toString(), Objects.requireNonNull(passwordText.getText()).toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {

                                    String uid = Objects.requireNonNull(task.getResult().getUser()).getUid();
                                    Users user = new Users(Objects.requireNonNull(usernameText.getText()).toString(), emailText.getText().toString(), passwordText.getText().toString(), uid);
                                    database.getReference().child("Users").child(uid).setValue(user);

                                    Intent intent = new Intent(getActivity(), Account_created_popup_activity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    requireActivity().finish();

                                    Toast.makeText(getActivity(), "User signed up successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        //FOR PHONE SIGN UP
//        call.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), Phone_send_otp.class);
//                startActivity(intent);
//            }
//        });
    }

    private void googleSignUp() {
        googleSignInClient.signOut().addOnCompleteListener(requireActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Start sign in process after sign out is complete
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.show();
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (Exception e) {
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    FirebaseUser user = auth.getCurrentUser();

                    if (user != null) {
                        Users users = new Users(user.getDisplayName(), user.getEmail(), "", user.getUid());

                        if (user.getPhotoUrl() != null) {
                            users.setProfilePhoto(user.getPhotoUrl().toString());
                        }

                        String uid = user.getUid();
                        database.getReference().child("Users").child(uid).setValue(users);

                        Intent intent = new Intent(getActivity(), Account_created_popup_activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        requireActivity().finish();
                    }
                } else {
                    Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
