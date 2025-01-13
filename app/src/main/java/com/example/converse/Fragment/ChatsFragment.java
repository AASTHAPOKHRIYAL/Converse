package com.example.converse.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.converse.Activity.SignUp;
import com.example.converse.Activity.ZoomProfilePhoto;
import com.example.converse.Adapter.UsersAdapter;
import com.example.converse.Models.Users;
import com.example.converse.R;
import com.example.converse.databinding.FragmentChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ChatsFragment extends Fragment {
    FragmentChatsBinding binding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;
    Toolbar toolbar, search_toolbar;
    ActionBarDrawerToggle toggle;
    FirebaseAuth auth;
    RecyclerView chatRecyclerView;
    TextView heading;
    TextView chats_text;
    UsersAdapter adapter;
    EditText search_view;
    ImageView profile_photo;
    private String str = "";

//    private boolean isToolbarHidden = false;

    public ChatsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentChatsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Initialize RecyclerView and other UI elements
        chatRecyclerView = view.findViewById(R.id.chat_recycler_view);
        toolbar = view.findViewById(R.id.toolbar);
        search_toolbar = view.findViewById(R.id.search_toolbar);
        heading = view.findViewById(R.id.heading);
        chats_text = view.findViewById(R.id.chats_text);
        search_view = view.findViewById(R.id.search_view);
        profile_photo = view.findViewById(R.id.profile_image);


        adapter = new UsersAdapter(list, getContext());
        chatRecyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatRecyclerView.setLayoutManager(layoutManager);
        // Initialize database and auth
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

//            chatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    if (dy > 0 && !isToolbarHidden) {
//                        toolbar.animate().alpha(0f).setDuration(200).withEndAction(() -> toolbar.setVisibility(View.GONE)).start();
//                        heading.animate().alpha(0f).setDuration(200).withEndAction(() -> heading.setVisibility(View.GONE)).start();
//                        isToolbarHidden = true;
//                    } else if (dy < 0 && isToolbarHidden) {
//                        toolbar.setVisibility(View.VISIBLE);
//                        toolbar.animate().alpha(1f).setDuration(200).start();
//                        heading.setVisibility(View.VISIBLE);
//                        heading.animate().alpha(1f).setDuration(200).start();
//                        isToolbarHidden = false;
//                    }
//                }
//            });

        // Listen for chat list updates
        Log.d("TAG", "Reached here: ");
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

//                    String currentUserId = FirebaseAuth.getInstance().getUid();
                Log.d("TAG", "Current User ID: ");

//                    if (currentUserId != null) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);
//                            if (user != null) {
//
//                                String userId = user.getUserId(dataSnapshot.getKey());
//
//                                if (userId != null && !userId.equals(currentUserId)) {
                    user.getUserId(dataSnapshot.getKey());
                    Log.d("Flag", "Receiver id : " + user.getUserId());
                    list.add(user);
                    Log.d("TAG", "Data fetched again: ");
                }
//                            }
//                        }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Failed to read chat list data.", error.toException());
                Toast.makeText(getContext(), "Failed to load chat data. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
//
        // Accessing header view of navigation drawer
        View headerView = binding.navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.user_name);
        ImageView profile_image = headerView.findViewById(R.id.profile_image);

        // Listen for profile changes and update navigation drawer
        database.getReference().child("Users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("TAG", "Data fetched: " + snapshot.toString());

                        Users users = snapshot.getValue(Users.class);
                        if (users != null) {
                            Picasso.get()
                                    .load(users.getProfilePhoto())
                                    .placeholder(R.drawable.user)
                                    .into(profile_image);
                            userName.setText(users.getUserName());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("TAG", "Failed to read profile data.", error.toException());
                    }
                });

        toggle = new ActionBarDrawerToggle(getActivity(), binding.drawer, toolbar, R.string.OpenDrawer, R.string.CloseDrawer);
        binding.drawer.addDrawerListener(toggle);
        toggle.syncState();

        toggle.setDrawerIndicatorEnabled(false);
        toolbar.setNavigationIcon(R.drawable.baseline_menu_24);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> {
            if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
                binding.drawer.closeDrawer(GravityCompat.START);
            } else {
                binding.drawer.openDrawer(GravityCompat.START);
            }
        });


        binding.navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.logout) {
                auth.signOut();
                Intent intent = new Intent(getActivity(), SignUp.class);
                startActivity(intent);

            } else if (id == R.id.settings) {
                // OPENING PROFILE FRAGMENT FROM CHATS FRAGMENT WHEN SETTINGS IS CLICKED
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, profileFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            } else if (id == R.id.help) {
                Toast.makeText(getContext(), "Help clicked", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Invite friends clicked", Toast.LENGTH_SHORT).show();
            }
            return true;
        });

//        search_view.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                str=s.toString();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });


        return view;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        Fire
//    }
}


//    @Override
//    public void onBackPressed() {
//        if (binding.drawer.isDrawerOpen(GravityCompat.START)) {
//            binding.drawer.closeDrawer(GravityCompat.START);
//        } else {
//            finishAffinity(requireActivity());
//        }
//    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.search_menu, menu);
//        MenuItem menuItem = menu.findItem(R.id.search_toolbar);
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        assert searchView != null;
//        searchView.setQueryHint("Search");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                adapter.filterList(newText);
//                return false;
//            }
//        });
//        super.onCreateOptionsMenu(menu, inflater);
//    }

