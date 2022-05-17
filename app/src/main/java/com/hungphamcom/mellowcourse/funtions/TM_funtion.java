package com.hungphamcom.mellowcourse.funtions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.viewpager2.widget.ViewPager2;

import com.hungphamcom.mellowcourse.MainScreen;
import com.hungphamcom.mellowcourse.R;
import com.hungphamcom.mellowcourse.Search_Item;
import com.hungphamcom.mellowcourse.add_new_item;
import com.hungphamcom.mellowcourse.ui.UserCart;
import com.hungphamcom.mellowcourse.ui.UserList;
import com.hungphamcom.mellowcourse.ui.item_detail;

public class TM_funtion {

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }

    private int check;

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

    public int check(Context context, AlertDialog.Builder builder, AlertDialog dialog
    ,LayoutInflater inflater ){
        builder=new AlertDialog.Builder(context);
        inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.search_pop_up_box,null);

        EditText editText = view.findViewById(R.id.search_input);
        Button cancel=view.findViewById(R.id.cancel_search_popUp);
        Button search=view.findViewById(R.id.search_search_popUp);

        builder.setView(view);
        dialog=builder.create();
        dialog.show();

        AlertDialog finalDialog = dialog;
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
            }
        });
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search=editText.getText().toString().trim();
                if(!search.isEmpty()){
                    check=1;
                }else {
                    Toast.makeText(context,"Type the item you want to search"
                    ,Toast.LENGTH_SHORT).show();
                    check=0;
                }
            }
        });
        return check;
    }

    public Intent checkSearch(int check,Context context,LayoutInflater inflater){
        inflater= LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.search_pop_up_box,null);

        Button search=view.findViewById(R.id.search_search_popUp);

        String searchText=search.getText().toString().trim();

        if(check==1){
            Intent intent1=new Intent(context, add_new_item.class);
            intent1.putExtra("searchItem", searchText);
            check=0;
            return intent1;
        }
        check=0;
        return null;
    }
}
