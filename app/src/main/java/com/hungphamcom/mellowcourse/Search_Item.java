package com.hungphamcom.mellowcourse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hungphamcom.mellowcourse.adapter.shopItemRecyclerAdapter;
import com.hungphamcom.mellowcourse.model.Item;
import com.hungphamcom.mellowcourse.ui.UserList;
import com.hungphamcom.mellowcourse.ui.item_detail;

import java.util.ArrayList;
import java.util.Locale;

public class Search_Item extends AppCompatActivity {
    private DatabaseReference ref;
    private ArrayList<Item> itemsList;
    private RecyclerView recyclerView;
    private SearchView searchView;

    private shopItemRecyclerAdapter shopItemRecyclerAdapter;
    private shopItemRecyclerAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        searchView=findViewById(R.id.search_bar_Search_item);
        recyclerView=findViewById(R.id.recyclerview_search_item);
        ref= FirebaseDatabase.getInstance().getReference().child("Item");

    }

    @Override
    protected void onStart() {
        super.onStart();
        itemsList=new ArrayList<>();
        if(ref!=null){
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot ds: snapshot.getChildren()){
                            itemsList.add(ds.getValue(Item.class));
                        }
                        setOnClickListener();
                        shopItemRecyclerAdapter=new shopItemRecyclerAdapter(Search_Item.this
                        ,itemsList,listener);
                        recyclerView.setAdapter(shopItemRecyclerAdapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Search_Item.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        if(searchView!=null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    private void search(String str) {
        ArrayList<Item> myList=new ArrayList<>();
        if(itemsList.isEmpty()){
            return;
        }
        for(Item object: itemsList){
            if(object.getName().toLowerCase(Locale.ROOT).contains(str.toLowerCase(Locale.ROOT)))
            {
                myList.add(object);
            }
        }
        shopItemRecyclerAdapter shopItemRecyclerAdapter1=new shopItemRecyclerAdapter(Search_Item.this
                ,itemsList,listener);
        recyclerView.setAdapter(shopItemRecyclerAdapter1);
    }

    private void setOnClickListener() {
        listener=new shopItemRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent=new Intent(Search_Item.this, item_detail.class);
                intent.putExtra("itemName", itemsList.get(position).getName());
                intent.putExtra("itemPrice", itemsList.get(position).getPrice());
                intent.putExtra("itemDescription", itemsList.get(position).getDescription());
                intent.putExtra("imageUrl", itemsList.get(position).getImageUrl());
                intent.putExtra("pplPurchase", itemsList.get(position).getPurchase());
                intent.putExtra("review", itemsList.get(position).getReview());
                intent.putExtra("pplReview", itemsList.get(position).getPplReview());
                intent.putExtra("userId", itemsList.get(position).getUserId());
                intent.putExtra("userName", itemsList.get(position).getUsername());
                intent.putExtra("itemId", itemsList.get(position).getItemId());
                startActivity(intent);
            }
        };
    }
}