package com.example.converse.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.converse.Adapter.ViewPagerAdapter;
import com.example.converse.R;

import org.w3c.dom.Text;

public class NavigationActivity extends AppCompatActivity {
    ViewPager viewPager;
    LinearLayout dotIndicator;
    ViewPagerAdapter viewPagerAdapter;
    Button next, back, skip;
    TextView dots[];

    ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            setDotIndicator(position);
            if(position>0)
            {
                back.setVisibility(View.VISIBLE);
            }
            else
            {
                back.setVisibility(View.INVISIBLE);
            }
            if(position==2)
            {
                next.setText("Finish");
            }
            else
            {
                next.setText("Next");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        back = findViewById(R.id.back_button);
        next = findViewById(R.id.next_button);
        skip = findViewById(R.id.skip_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItem(0) > 0) {
                    viewPager.setCurrentItem(getItem(-1), true);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(1);
                if (current < viewPagerAdapter.getCount()) {
                    viewPager.setCurrentItem(current, true);
                } else {
                    Intent intent = new Intent(getApplicationContext(), GetStarted.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.slide_view_pager);
        dotIndicator = (LinearLayout) findViewById(R.id.dot_indicator);

        viewPagerAdapter = new ViewPagerAdapter(getApplicationContext());
        viewPager.setAdapter(viewPagerAdapter);

        setDotIndicator(0);
        viewPager.setOnPageChangeListener(viewPagerListener);
    }

    public void setDotIndicator(int position)
    {
        dots = new TextView[3];
        dotIndicator.removeAllViews();

        for(int i=0;i<dots.length;i++)
        {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.metallic_grey, getApplicationContext().getTheme()));
            dotIndicator.addView(dots[i]);
        }
        dots[position].setTextColor(getResources().getColor(R.color.purple, getApplicationContext().getTheme()));
    }

    private int getItem(int i)
    {
        return viewPager.getCurrentItem() + i;
    }
}