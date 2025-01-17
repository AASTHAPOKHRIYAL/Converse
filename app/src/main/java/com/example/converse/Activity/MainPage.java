package com.example.converse.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.converse.Fragment.CallFragment;
import com.example.converse.Fragment.ChatsFragment;
import com.example.converse.OnBackPressedListener;
import com.example.converse.Fragment.ProfileFragment;
import com.example.converse.R;
import com.example.converse.databinding.ActivityMainPageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainPage extends AppCompatActivity {

    ActivityMainPageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Null-check to avoid NullPointerException
        BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);
        if (bottom_navigation == null) {
            Log.e("MainPage", "BottomNavigationView is null. Check the layout.");
            return;
        } else {
            bottom_navigation.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                if (id == R.id.chats) {
                    loadFragment(new ChatsFragment());
                } else if (id == R.id.profile) {
                    loadFragment(new ProfileFragment());
                } else {
                    loadFragment(new CallFragment());
                }
                return true;
            });
            bottom_navigation.setSelectedItemId(R.id.chats);
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(v);
            if (windowInsets != null) {
                Insets systemBarsInsets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                // Use systemBarsInsets for adjusting view paddings/margins if needed
            }
            return insets;
        });
    }

    // Load the fragment based on user selection
    public void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_layout, fragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (fragment instanceof OnBackPressedListener) {
            ((OnBackPressedListener) fragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}
