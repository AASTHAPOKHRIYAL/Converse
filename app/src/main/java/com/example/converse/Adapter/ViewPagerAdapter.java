package com.example.converse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.converse.R;

public class ViewPagerAdapter extends PagerAdapter {
    Context context;
    int sliderImages[]={R.drawable.chatting_feature, R.drawable.call_feature};
    int sliderTitle[]={R.string.break_the_ice, R.string.seemless_calls};
    int sliderDesc[]={R.string.chat_with_your_friends_and_family ,R.string.voice_calls_or_video_calls};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }


    @Override
    public int getCount() {
        return sliderTitle.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slider_screen, container, false);

        ImageView sliderImg = (ImageView) view.findViewById(R.id.slider_image);
        TextView sliderTitle = (TextView) view.findViewById(R.id.slider_title);
        TextView sliderDesc = (TextView) view.findViewById(R.id.slider_description);

        sliderImg.setImageResource(sliderImages[position]);
        sliderTitle.setText(this.sliderTitle[position]);
        sliderDesc.setText(this.sliderDesc[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
