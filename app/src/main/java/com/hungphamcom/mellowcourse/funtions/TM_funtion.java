package com.hungphamcom.mellowcourse.funtions;

import android.content.Context;
import android.content.Intent;

import androidx.viewpager2.widget.ViewPager2;

import com.hungphamcom.mellowcourse.MainScreen;
import com.hungphamcom.mellowcourse.add_new_item;
import com.hungphamcom.mellowcourse.ui.UserCart;
import com.hungphamcom.mellowcourse.ui.item_detail;

public class TM_funtion {

    public void logoButton(ViewPager2 vp){
        vp.setCurrentItem(0);
    }

    public Intent addItem(Context context){
        Intent intent=new Intent(context, add_new_item.class);
        return intent;
    }

    public Intent itemCart(Context context){
        Intent intent=new Intent(context, UserCart.class);
        return intent;
    }

    public Intent backToMain(Context context){
        Intent intent=new Intent(context
                , MainScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

}
