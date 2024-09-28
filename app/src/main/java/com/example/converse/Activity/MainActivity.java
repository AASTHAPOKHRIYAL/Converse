package com.example.converse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.converse.R;

public class MainActivity extends AppCompatActivity {
TextView textView2, textView1, textView3;
ImageView main_icon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        main_icon = findViewById(R.id.main_icon);

        Animation appNameAnimation = AnimationUtils.loadAnimation(this, R.anim.app_name_animation);
        Animation taglineAnimation = AnimationUtils.loadAnimation(this, R.anim.tagline_animation);
        Animation app_image = AnimationUtils.loadAnimation(this, R.anim.image_animation);

        textView2.startAnimation(appNameAnimation);
        textView1.startAnimation(taglineAnimation);
        textView3.startAnimation(taglineAnimation);
        main_icon.startAnimation(app_image);
//        auth=FirebaseAuth.getInstance();

        new Handler().postDelayed(() -> {
//            FirebaseUser currUser = auth.getCurrentUser();
            Intent intent;
//            if(currUser != null)
//            {
//                intent = new Intent(getApplicationContext(), MainPage.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            }
//            else
//            {
                intent = new Intent(getApplicationContext(), Features.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            }

            startActivity(intent);
            overridePendingTransition(R.anim.features_right_slide_animation, R.anim.slapsh_screen_slid_animation);
            finish();
        },6000);
    }

    }