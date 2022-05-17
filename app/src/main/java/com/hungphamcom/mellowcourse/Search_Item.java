package com.hungphamcom.mellowcourse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hungphamcom.mellowcourse.adapter.shopItemRecyclerAdapter;
import com.hungphamcom.mellowcourse.funtions.TM_funtion;
import com.hungphamcom.mellowcourse.model.Item;
import com.hungphamcom.mellowcourse.ui.UserList;
import com.hungphamcom.mellowcourse.ui.item_detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Search_Item extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference itemDB=db.collection("Item");
    private String searchTX;
    private List<Item> itemList;

    private TM_funtion tm_funtion=new TM_funtion();

    private ImageView backBtn,addBtn,searchBtn,cartBtn;

    private shopItemRecyclerAdapter shopItemRecyclerAdapter;
    private shopItemRecyclerAdapter.RecyclerViewClickListener listener;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);


        itemList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerview_search_item);
        backBtn=findViewById(R.id.back_to_mainScreen_searchItem);
        addBtn=findViewById(R.id.addItem_searchItem);
        searchBtn=findViewById(R.id.search_item_searchItem);
        cartBtn=findViewById(R.id.itemCart_searchItem);

        backBtn.setOnClickListener(this);
        addBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        cartBtn.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            searchTX=extras.getString("searchItem");
            Toast.makeText(Search_Item.this,searchTX,Toast.LENGTH_LONG).show();
        }

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(Search_Item.this));

        itemDB.whereEqualTo("name",searchTX)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for(QueryDocumentSnapshot items : queryDocumentSnapshots){
                        Item item = items.toObject(Item.class);
                        itemList.add(item);
                    }
                    Log.e("search", "onSuccess: item not showing" );
                    setOnClickListener();
                    shopItemRecyclerAdapter=new shopItemRecyclerAdapter(Search_Item.this
                            ,itemList,listener);
                    recyclerView.setAdapter(shopItemRecyclerAdapter);
                    shopItemRecyclerAdapter.notifyDataSetChanged();
                }else {
                    Log.e("search", "onSuccess: no item in shop" );
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
            }

    private void setOnClickListener() {
        listener=new shopItemRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent=new Intent(Search_Item.this, item_detail.class);
                intent.putExtra("itemName", itemList.get(position).getName());
                intent.putExtra("itemPrice", itemList.get(position).getPrice());
                intent.putExtra("itemDescription", itemList.get(position).getDescription());
                intent.putExtra("imageUrl", itemList.get(position).getImageUrl());
                intent.putExtra("pplPurchase", itemList.get(position).getPurchase());
                intent.putExtra("review", itemList.get(position).getReview());
                intent.putExtra("pplReview", itemList.get(position).getPplReview());
                intent.putExtra("userId", itemList.get(position).getUserId());
                intent.putExtra("userName", itemList.get(position).getUsername());
                intent.putExtra("itemId", itemList.get(position).getItemId());
                startActivity(intent);
            }
        };
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_to_mainScreen_searchItem:
                finish();
                startActivity(tm_funtion.backToMain(Search_Item.this));
                break;
            case R.id.addItem_searchItem:
                finish();
                startActivity(tm_funtion.addItem(Search_Item.this));
                break;
            case R.id.search_item_searchItem:
                builder=new AlertDialog.Builder(Search_Item.this);
                inflater= LayoutInflater.from(Search_Item.this);
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
                            finish();
                            Intent intent1=new Intent(Search_Item.this, Search_Item.class);
                            intent1.putExtra("searchItem", search);
                            startActivity(intent1);
                        }else {
                            Toast.makeText(Search_Item.this,"Type the item you want to search"
                                    ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.itemCart_searchItem:
                finish();
                startActivity(tm_funtion.itemCart(Search_Item.this));
                break;
        }
    }
}