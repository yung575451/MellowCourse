package com.hungphamcom.mellowcourse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.hungphamcom.mellowcourse.adapter.viewPagerAdapter;
import com.hungphamcom.mellowcourse.fragment.HomeFragment;
import com.hungphamcom.mellowcourse.funtions.TM_funtion;
import com.hungphamcom.mellowcourse.ui.UserCart;
import com.hungphamcom.mellowcourse.util.UserApi;

public class MainScreen extends AppCompatActivity implements View.OnClickListener  {
    public static ViewPager2 mViewPager;
    private TabLayout mTabLayout;
    private ImageView mLogo_MainScreen;
    private TextView buyNowBtn;
    private ImageView addItem,cartItem;
    private UserApi userApi;
    private TM_funtion funtion=new TM_funtion();
    private com.hungphamcom.mellowcourse.adapter.viewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        addItem=findViewById(R.id.addItem_mainScreen);
        cartItem=findViewById(R.id.itemCart_mainScreen);

        mViewPager=findViewById(R.id.viewpager);
        mTabLayout=findViewById(R.id.tabLayout);

        mLogo_MainScreen=findViewById(R.id.logo_mainScreen);
        mLogo_MainScreen.setOnClickListener(this);
        initPagerAdapter();
        tabLayoutValueSetter();
        addItem.setOnClickListener(this);
        cartItem.setOnClickListener(this);

        if(UserApi.getInstance().getStatus().equals("user")){
            addItem.setVisibility(View.INVISIBLE);
        }

        Log.d("mainScreen", "Username: "+UserApi.getInstance().getUsername()
              +  "UserId: "+UserApi.getInstance().getUserId()
        +"UserStatus: "+UserApi.getInstance().getStatus());

    }
    private void tabLayoutValueSetter() {


        final String[] tabTitles = {"Home", "Shop", "Wishlist","Profile"};//put titles based on your need
        final int[] tabIcons = {R.drawable.ic_home_2, R.drawable.ic_shop
                , R.drawable.ic_akar_icons_heart,R.drawable.ic_frame};


        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mTabLayout, mViewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabTitles[position]);
                        tab.setIcon(tabIcons[position]);
                        tab.select();
                    }
                }
        );
        tabLayoutMediator.attach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.logo_mainScreen:
                funtion.logoButton(mViewPager);
                break;
            case R.id.addItem_mainScreen:
                //Toast.makeText(this, "add Item has been pressed", Toast.LENGTH_SHORT).show();
                startActivity(funtion.addItem(MainScreen.this));
                break;
            case R.id.itemCart_mainScreen:
                startActivity(funtion.itemCart(MainScreen.this));
                break;
        }
    }

    public void initPagerAdapter(){
        viewPagerAdapter=new viewPagerAdapter(MainScreen.this);
        mViewPager.setAdapter(viewPagerAdapter);
    }

    public void showAddItem(){
        addItem.setVisibility(View.VISIBLE);
        Toast.makeText(MainScreen.this,"You have become a seller",Toast.LENGTH_LONG).show();
    }
}