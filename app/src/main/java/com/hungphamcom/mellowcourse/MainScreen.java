package com.hungphamcom.mellowcourse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    private ImageView addItem,cartItem,searchItem;
    private UserApi userApi;
    private TM_funtion funtion=new TM_funtion();
    private com.hungphamcom.mellowcourse.adapter.viewPagerAdapter viewPagerAdapter;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        addItem=findViewById(R.id.addItem_mainScreen);
        cartItem=findViewById(R.id.itemCart_mainScreen);

        mViewPager=findViewById(R.id.viewpager);
        mTabLayout=findViewById(R.id.tabLayout);
        searchItem=findViewById(R.id.search_item_mainScreen);

        mLogo_MainScreen=findViewById(R.id.logo_mainScreen);
        mLogo_MainScreen.setOnClickListener(this);
        initPagerAdapter();
        tabLayoutValueSetter();
        addItem.setOnClickListener(this);
        cartItem.setOnClickListener(this);
        searchItem.setOnClickListener(this);

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
            case R.id.search_item_mainScreen:
                builder=new AlertDialog.Builder(MainScreen.this);
                inflater= LayoutInflater.from(MainScreen.this);
                View view1=inflater.inflate(R.layout.search_pop_up_box,null);

                EditText editText = view1.findViewById(R.id.search_input);
                Button cancel=view1.findViewById(R.id.cancel_search_popUp);
                Button search=view1.findViewById(R.id.search_search_popUp);

                builder.setView(view1);
                dialog=builder.create();
                dialog.show();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                search.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String search=editText.getText().toString().trim();
                        if(!search.isEmpty()){
                            Intent intent1=new Intent(MainScreen.this, Search_Item.class);
                            intent1.putExtra("searchItem", search);
                            startActivity(intent1);
                        }else {
                            Toast.makeText(MainScreen.this,"Type the item you want to search"
                                    ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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