package com.hungphamcom.mellowcourse.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hungphamcom.mellowcourse.fragment.HomeFragment;
import com.hungphamcom.mellowcourse.fragment.ProfileFragment;
import com.hungphamcom.mellowcourse.fragment.ShopFragment;
import com.hungphamcom.mellowcourse.fragment.WishlistFragment;

public class viewPagerAdapter extends FragmentStateAdapter {
    public viewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new ShopFragment();
            case 2:
                return new WishlistFragment();
            case 3:
                return new ProfileFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }



}
