package com.hungphamcom.mellowcourse.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.hungphamcom.mellowcourse.MainScreen;
import com.hungphamcom.mellowcourse.R;
import com.hungphamcom.mellowcourse.add_new_item;
import com.hungphamcom.mellowcourse.model.Item;
import com.hungphamcom.mellowcourse.model.Wishlist;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;

public class item_detail extends AppCompatActivity implements View.OnClickListener {
    private ImageView backToMainBtn;
    private ImageView addItem;


    private String image;
    private ImageView imageUrl;
    private TextView itemName;
    private TextView sellerName;
    private TextView itemDescription;
    private int review;
    private String userId;
    private TextView pplReview;
    private TextView pplPurchase;
    private TextView price;
    private TextView sellerNameDetail;
    private TextView buyNowBtn;
    private TextView addToWishList;
    private TextView addToCart;
    private ImageView star1,star2,star3,star4,star5;
    private String itemId = null;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //connect to fire store;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference userWishList=db.collection("WishList");

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);


        itemName=findViewById(R.id.item_name_item_detail);
        imageUrl=findViewById(R.id.item_image_item_detail);
        sellerName=findViewById(R.id.seller_name_item_detail);
        itemDescription=findViewById(R.id.item_description_item_detail);
        price=findViewById(R.id.item_price_item_detail);
        pplReview=findViewById(R.id.numberOfPeopleRated_item_detail);
        pplPurchase=findViewById(R.id.numberOfPeopleBuy_item_detail);
        sellerNameDetail=findViewById(R.id.seller_name_tag_item_profile);
        buyNowBtn=findViewById(R.id.buy_now_Btn_item_detail);
        addToWishList=findViewById(R.id.add_to_wishList_Btn_item_detail);
        addToCart=findViewById(R.id.add_to_cart_Btn_item_detail);
        star1=findViewById(R.id.star1_review_item_detail);
        star2=findViewById(R.id.star2_review_item_detail);
        star3=findViewById(R.id.star3_review_item_detail);
        star4=findViewById(R.id.star4_review_item_detail);
        star5=findViewById(R.id.star5_review_item_detail);

        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            itemName.setText(extras.getString("itemName"));
            image=extras.getString("imageUrl");
            sellerName.setText(extras.getString("userName"));
            itemDescription.setText(extras.getString("itemDescription"));
            price.setText("$"+String.valueOf(extras.getInt("itemPrice")));
            pplReview.setText("("+String.valueOf(extras.getInt("pplReview"))+")");
            pplPurchase.setText(String.valueOf(extras.getInt("pplPurchase"))+" Purchase");
            sellerNameDetail.setText(extras.getString("userName"));
            review=extras.getInt("review");
            userId=extras.getString("userId");
            itemId=extras.getString("itemId");

        }
        switch (review){
            case 0:
                star1.setBackgroundResource(R.drawable.ic_emptystar_review);
                star2.setBackgroundResource(R.drawable.ic_emptystar_review);
                star3.setBackgroundResource(R.drawable.ic_emptystar_review);
                star4.setBackgroundResource(R.drawable.ic_emptystar_review);
                star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                break;
            case 1:
                star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                star2.setBackgroundResource(R.drawable.ic_emptystar_review);
                star3.setBackgroundResource(R.drawable.ic_emptystar_review);
                star4.setBackgroundResource(R.drawable.ic_emptystar_review);
                star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                break;
            case 2:
                star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                star3.setBackgroundResource(R.drawable.ic_emptystar_review);
                star4.setBackgroundResource(R.drawable.ic_emptystar_review);
                star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                break;
            case 3:
                star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                star3.setBackgroundResource(R.drawable.ic_fullstar_review);
                star4.setBackgroundResource(R.drawable.ic_emptystar_review);
                star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                break;
            case 4:
                star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                star3.setBackgroundResource(R.drawable.ic_fullstar_review);
                star4.setBackgroundResource(R.drawable.ic_fullstar_review);
                star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                break;
            case 5:
                star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                star3.setBackgroundResource(R.drawable.ic_fullstar_review);
                star4.setBackgroundResource(R.drawable.ic_fullstar_review);
                star5.setBackgroundResource(R.drawable.ic_fullstar_review);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + review);
        }
        Picasso.get().load(image)
                .fit().into(imageUrl);
        Log.d("passItem", "onCreate: "+itemId);
        backToMainBtn=findViewById(R.id.back_to_mainScreen_itemDetail);
        addItem=findViewById(R.id.addItem_item_detail);


        backToMainBtn.setOnClickListener(this);
        addItem.setOnClickListener(this);
        addToWishList.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_to_mainScreen_itemDetail:
                finish();
                Log.d("itemDetail", "onClick: "+"MainScreen");
                break;
            case R.id.addItem_item_detail:
                finish();
                Log.d("itemDetail", "onClick: "+"additem");
                startActivity(new Intent(item_detail.this, add_new_item.class));
                break;
            case R.id.add_to_wishList_Btn_item_detail:
                addToWishList();
                break;
        }
    }

    private void addToWishList() {
        Wishlist wishlist=new Wishlist();
        wishlist.setItemId(itemId);
        userWishList.document(userId).set(wishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(item_detail.this,"Add to WishList success",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
}