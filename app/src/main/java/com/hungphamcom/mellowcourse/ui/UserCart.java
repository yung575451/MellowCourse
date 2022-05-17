package com.hungphamcom.mellowcourse.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;
import com.hungphamcom.mellowcourse.MainScreen;
import com.hungphamcom.mellowcourse.R;
import com.hungphamcom.mellowcourse.Search_Item;
import com.hungphamcom.mellowcourse.adapter.cartRecyclerAdapter;
import com.hungphamcom.mellowcourse.adapter.shopItemRecyclerAdapter;
import com.hungphamcom.mellowcourse.funtions.TM_funtion;
import com.hungphamcom.mellowcourse.model.Item;
import com.hungphamcom.mellowcourse.util.UserApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCart extends AppCompatActivity implements View.OnClickListener {
    private ImageView backToMainBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private TextView totalPriceShow;
    private LinearLayout buyCard;

    private int totalPrice=0;
    private String userid= UserApi.getInstance().getUserId();
    private int pplPurchase;

    private TM_funtion tm_funtion=new TM_funtion();

    private List<Item> itemList;
    private RecyclerView recyclerView;
    private com.hungphamcom.mellowcourse.adapter.cartRecyclerAdapter cartRecyclerAdapter;
    private cartRecyclerAdapter.RecyclerViewClickListener listener;

    private ArrayList<String> list;
    private TextView buyBtn;

    private CollectionReference collectionReference=db.collection("Item");
    private TextView noItemInShop;

    private CollectionReference userCartList = db.collection("Cart");

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cart);
        
        firebaseAuth= FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        backToMainBtn=findViewById(R.id.back_to_mainScreen_userCart);
        totalPriceShow=findViewById(R.id.total_price_user_cart);
        buyCard=findViewById(R.id.card_view_user_cart);
        buyBtn=findViewById(R.id.buyBtn_user_cart);

        noItemInShop=findViewById(R.id.noItemInCart);
        itemList=new ArrayList<>();

        recyclerView=findViewById(R.id.recyclerview_user_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserCart.this));

        getCartItem();

        backToMainBtn.setOnClickListener(this);
        buyBtn.setOnClickListener(this);

    }

    private void getCartItem() {
        Log.d("WishList", "getUID: "+userid);
        userCartList.document(userid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document=task.getResult();
                            if(document.exists()){
                                list=(ArrayList<String>) document.get("itemId");
                                if(list.size()==0){
                                    noItemInShop.setVisibility(View.VISIBLE);
                                    buyCard.setVisibility(View.INVISIBLE);
                                }
                                for(int i=0;i<list.size();i++){
                                    String x= list.get(i);
                                    collectionReference.whereEqualTo("itemId",x)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if(!queryDocumentSnapshots.isEmpty()) {
                                                        for (QueryDocumentSnapshot items : queryDocumentSnapshots) {
                                                            Item item = items.toObject(Item.class);
                                                            totalPrice=totalPrice+item.getPrice();
                                                            itemList.add(item);
                                                        }
                                                        totalPriceShow.setText("$" + String.valueOf(totalPrice));
                                                        setOnClickListener();
                                                        cartRecyclerAdapter = new cartRecyclerAdapter(UserCart.this,
                                                                itemList,listener);
                                                        recyclerView.setAdapter(cartRecyclerAdapter);
                                                        cartRecyclerAdapter.notifyDataSetChanged();



                                                    }else{

                                                        noItemInShop.setVisibility(View.VISIBLE);

                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            }
                        }
                    }
                });
    }

    private void setOnClickListener() {
        listener=new cartRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                switch (v.getId()){
                    case R.id.item_item_detail:
                        Intent intent=new Intent(UserCart.this, item_detail.class);
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
                        break;
                    case R.id.delete_item_shop_fragment:
                        Item item=itemList.get(position);

                        userCartList.document(item.getUserId()).update("itemId", FieldValue.arrayRemove(item.getItemId()));
                        finish();
                        Intent intent1=new Intent(UserCart.this,UserCart.class);
                        startActivity(intent1);
                        break;
                }

            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_to_mainScreen_userCart:
                finish();
                startActivity(tm_funtion.backToMain(UserCart.this));
                break;
            case R.id.buyBtn_user_cart:
                buyItem();
                break;
            case R.id.search_user_cart:
                builder=new AlertDialog.Builder(UserCart.this);
                inflater= LayoutInflater.from(UserCart.this);
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
                            Intent intent1=new Intent(UserCart.this, UserCart.class);
                            intent1.putExtra("searchItem", search);
                            startActivity(intent1);
                        }else {
                            Toast.makeText(UserCart.this,"Type the item you want to search"
                                    ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    private void buyItem() {
        builder=new AlertDialog.Builder(UserCart.this);
        inflater= LayoutInflater.from(UserCart.this);
        View view=inflater.inflate(R.layout.popup_are_you_sure_option,null);

        TextView title=view.findViewById(R.id.title_pop_up);
        TextView no=view.findViewById(R.id.no_pop_up_option);
        TextView yes=view.findViewById(R.id.yes_pop_up_option);

        title.setText("Are you sure?");

        builder.setView(view);
        dialog=builder.create();
        dialog.show();

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                buySuccessful();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void buySuccessful() {
        userCartList.document(userid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document=task.getResult();
                            if(document.exists()){
                                list=(ArrayList<String>) document.get("itemId");
                                if(list.size()==0){
                                }
                                for(int i=0;i<list.size();i++){
                                    String x= list.get(i);
                                    collectionReference.whereEqualTo("itemId",x)
                                            .get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    if(!queryDocumentSnapshots.isEmpty()) {
                                                        for (QueryDocumentSnapshot items : queryDocumentSnapshots) {
                                                            Item item = items.toObject(Item.class);
                                                            pplPurchase=item.getPurchase()+1;
                                                        }
                                                        totalPriceShow.setText("$" + String.valueOf(totalPrice));
                                                        setOnClickListener();
                                                        cartRecyclerAdapter = new cartRecyclerAdapter(UserCart.this,
                                                                itemList,listener);
                                                        recyclerView.setAdapter(cartRecyclerAdapter);
                                                        cartRecyclerAdapter.notifyDataSetChanged();



                                                    }else{

                                                        noItemInShop.setVisibility(View.VISIBLE);

                                                    }
                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful()){
                                                for(QueryDocumentSnapshot item:task.getResult()){
                                                    HashMap<String,Integer> updatePurchase = new HashMap<>();
                                                    updatePurchase.put("purchase",pplPurchase);
                                                    collectionReference.document(item.getId()).set(updatePurchase,SetOptions.merge());
                                                    Toast.makeText(UserCart.this,"You have purchase the item"
                                                            ,Toast.LENGTH_SHORT).show();
                                                    removeAllItemInCart();
                                                    Intent intent=new Intent(UserCart.this
                                                            , MainScreen.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            }
                        }
                    }
                });
    }

    private void removeAllItemInCart() {
        userCartList.document(userid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document=task.getResult();
                        if(document.exists()){
                            list=(ArrayList<String>) document.get("itemId");
                            if(list.size()==0){
                            }
                            for(int i=0;i<list.size();i++) {
                                String x = list.get(i);
                                userCartList.document(userid).update("itemId", FieldValue.arrayRemove(x));
                            }
                        }
                    };

                });
    }
}