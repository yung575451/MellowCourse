package com.hungphamcom.mellowcourse.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.hungphamcom.mellowcourse.R;
import com.hungphamcom.mellowcourse.adapter.viewPagerAdapter;


public class HomeFragment extends Fragment  {
    private TextView learnMoreBtn;
    private TextView buyNowBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        learnMoreBtn=view.findViewById(R.id.learnMoreBtn);
        buyNowBtn=view.findViewById(R.id.buyNowBtn);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewPager2 vp=getActivity().findViewById(R.id.viewpager);
        learnMoreBtn=view.findViewById(R.id.learnMoreBtn);
        buyNowBtn=view.findViewById(R.id.buyNowBtn);
        buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(1);
            }
        });
    }




}