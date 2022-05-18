package com.hungphamcom.mellowcourse.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.hungphamcom.mellowcourse.add_new_item;
import com.hungphamcom.mellowcourse.funtions.TM_funtion;
import com.hungphamcom.mellowcourse.model.Item;
import com.hungphamcom.mellowcourse.util.UserApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class item_detail extends AppCompatActivity implements View.OnClickListener {
    private ImageView backToMainBtn;
    private ImageView addItem;
    private ImageView searchItem;
    private ImageView cartItem;

    private String image;
    private ImageView imageUrl;
    private TextView itemName;
    private TextView sellerName;
    private TextView itemDescription;
    private int review;
    private String ownerId;
    private String userId;
    private TextView pplReview;
    private TextView pplPurchase;
    private TextView price;
    private TextView sellerNameDetail;
    private TextView buyNowBtn;
    private TextView addToWishList;
    private TextView addToCart;
    private ImageView star1, star2, star3, star4, star5,wishListIndicator;
    private String itemId = null;

    private int reviewScore=0;
    private int numberPplReview=0;
    private int numberPplPurchase=0;

    private TM_funtion funtion=new TM_funtion();

    private ArrayList<String> list;

    //connect to fire store;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference itemDB=db.collection("Item");
    private CollectionReference userWishList = db.collection("WishList");
    private CollectionReference userCart=db.collection("Cart");

    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        ownerId= UserApi.getInstance().getUserId();

        searchItem=findViewById(R.id.searchItem_item_detail);
        cartItem=findViewById(R.id.cartItem_item_detail);

        itemName = findViewById(R.id.item_name_item_detail);
        imageUrl = findViewById(R.id.item_image_item_detail);
        sellerName = findViewById(R.id.seller_name_item_detail);
        itemDescription = findViewById(R.id.item_description_item_detail);
        price = findViewById(R.id.item_price_item_detail);
        pplReview = findViewById(R.id.numberOfPeopleRated_item_detail);
        pplPurchase = findViewById(R.id.numberOfPeopleBuy_item_detail);
        sellerNameDetail = findViewById(R.id.seller_name_tag_item_profile);
        buyNowBtn = findViewById(R.id.buy_now_Btn_item_detail);
        addToWishList = findViewById(R.id.add_to_wishList_Btn_item_detail);
        addToCart = findViewById(R.id.add_to_cart_Btn_item_detail);
        star1 = findViewById(R.id.star1_review_item_detail);
        star2 = findViewById(R.id.star2_review_item_detail);
        star3 = findViewById(R.id.star3_review_item_detail);
        star4 = findViewById(R.id.star4_review_item_detail);
        star5 = findViewById(R.id.star5_review_item_detail);
        wishListIndicator=findViewById(R.id.wishlist_star_indicator_item_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemName.setText(extras.getString("itemName"));
            image = extras.getString("imageUrl");
            sellerName.setText(extras.getString("userName"));
            itemDescription.setText(extras.getString("itemDescription"));
            price.setText("$" + String.valueOf(extras.getInt("itemPrice")));
            pplReview.setText("(" + String.valueOf(extras.getInt("pplReview")) + ")");
            pplPurchase.setText(String.valueOf(extras.getInt("pplPurchase")) + " Purchase");
            sellerNameDetail.setText(extras.getString("userName"));
            review = extras.getInt("review");
            userId = extras.getString("userId");
            itemId = extras.getString("itemId");
            numberPplReview=extras.getInt("pplReview");
            numberPplPurchase=extras.getInt("pplPurchase");

        }
        switch (review) {
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
        Log.d("passItem", "onCreate: " + itemId);
        backToMainBtn = findViewById(R.id.back_to_mainScreen_itemDetail);
        addItem = findViewById(R.id.addItem_item_detail);

        checkItemInWishList();

        searchItem.setOnClickListener(this);
        cartItem.setOnClickListener(this);
        backToMainBtn.setOnClickListener(this);
        addItem.setOnClickListener(this);
        addToWishList.setOnClickListener(this);
        wishListIndicator.setOnClickListener(this);
        buyNowBtn.setOnClickListener(this);
        addToCart.setOnClickListener(this);
    }

    private void checkItemInWishList() {
        FirebaseFirestore.getInstance()
                .collection("WishList").document(ownerId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot=task.getResult();
                    if(documentSnapshot.exists()){
                        list=(ArrayList<String>) documentSnapshot.get("itemId");
                    }
                    for(int i=0;i<list.size();i++){
                        String x= list.get(i);
                        if(x.equals(itemId)){
                            addToWishList.setText("Remove from WishList");
                            wishListIndicator.setBackgroundResource(R.drawable.ic_wish_star_item_detail_full);
                        }
                    }
                }
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_to_mainScreen_itemDetail:
                startActivity(funtion.backToMain(item_detail.this));
                finish();
                break;
            case R.id.addItem_item_detail:
                finish();
                Log.d("itemDetail", "onClick: " + "additem");
                startActivity(funtion.addItem(item_detail.this));
                break;
            case R.id.add_to_wishList_Btn_item_detail:
                String check;
                check=addToWishList.getText().toString().trim();
                if (check.equals("Add to Wishlist")){
                    addToWishList();
                    addToWishList.setText("Remove from WishList");
                    wishListIndicator.setBackgroundResource(R.drawable.ic_wish_star_item_detail_full);

                }
                if (check.equals("Remove from WishList")){
                    removeItemWishList();
                    addToWishList.setText("Add to Wishlist");
                    wishListIndicator.setBackgroundResource(R.drawable.ic_wish_star_item_detail_empty);
                }
                break;
            case R.id.wishlist_star_indicator_item_detail:
                String checkIndicator;
                checkIndicator=addToWishList.getText().toString().trim();
                if (checkIndicator.equals("Add to Wishlist")){
                    addToWishList();
                    addToWishList.setText("Remove from WishList");
                    wishListIndicator.setBackgroundResource(R.drawable.ic_wish_star_item_detail_full);

                }
                if (checkIndicator.equals("Remove from WishList")){
                    removeItemWishList();
                    addToWishList.setText("Add to Wishlist");
                    wishListIndicator.setBackgroundResource(R.drawable.ic_wish_star_item_detail_empty);
                }
                break;
            case R.id.buy_now_Btn_item_detail:
                buyItemCheck();
                break;
            case R.id.add_to_cart_Btn_item_detail:
                addToCart();
                break;
            case R.id.searchItem_item_detail:
                builder=new AlertDialog.Builder(item_detail.this);
                inflater= LayoutInflater.from(item_detail.this);
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
                            Intent intent1=new Intent(item_detail.this, Search_Item.class);
                            intent1.putExtra("searchItem", search);
                            startActivity(intent1);
                        }else {
                            Toast.makeText(item_detail.this,"Type the item you want to search"
                                    ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case R.id.itemCart_searchItem:
                finish();
                startActivity(funtion.itemCart(item_detail.this));
                break;
        }

    }

    private void buyItemCheck() {
        builder=new AlertDialog.Builder(item_detail.this);
        inflater= LayoutInflater.from(item_detail.this);
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
                reviewProduct();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void reviewProduct() {
        builder=new AlertDialog.Builder(item_detail.this);
        inflater= LayoutInflater.from(item_detail.this);
        View view=inflater.inflate(R.layout.popup_review_product,null);

        ImageView star1=view.findViewById(R.id.star1_review_the_item);
        ImageView star2=view.findViewById(R.id.star2_review_the_item);
        ImageView star3=view.findViewById(R.id.star3_review_the_item);
        ImageView star4=view.findViewById(R.id.star4_review_the_item);
        ImageView star5=view.findViewById(R.id.star5_review_the_item);

        TextView okayBtn=view.findViewById(R.id.okayBtn_rate_the_item);

        builder.setView(view);
        dialog=builder.create();
        dialog.show();

        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                star2.setBackgroundResource(R.drawable.ic_emptystar_review);
                star3.setBackgroundResource(R.drawable.ic_emptystar_review);
                star4.setBackgroundResource(R.drawable.ic_emptystar_review);
                star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                reviewScore=1;
            }
        });

        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                star3.setBackgroundResource(R.drawable.ic_emptystar_review);
                star4.setBackgroundResource(R.drawable.ic_emptystar_review);
                star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                reviewScore=2;
            }
        });

        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                star3.setBackgroundResource(R.drawable.ic_fullstar_review);
                star4.setBackgroundResource(R.drawable.ic_emptystar_review);
                star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                reviewScore=3;
            }
        });

        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                star3.setBackgroundResource(R.drawable.ic_fullstar_review);
                star4.setBackgroundResource(R.drawable.ic_fullstar_review);
                star5.setBackgroundResource(R.drawable.ic_emptystar_review);
                reviewScore=4;
            }
        });

        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setBackgroundResource(R.drawable.ic_fullstar_review);
                star2.setBackgroundResource(R.drawable.ic_fullstar_review);
                star3.setBackgroundResource(R.drawable.ic_fullstar_review);
                star4.setBackgroundResource(R.drawable.ic_fullstar_review);
                star5.setBackgroundResource(R.drawable.ic_fullstar_review);
                reviewScore=5;
            }
        });

        okayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalScore;
                if(numberPplReview==0){
                    totalScore=reviewScore;
                }else {
                    totalScore=(review+reviewScore)/2;
                }

                numberPplReview=numberPplReview+1;
                numberPplPurchase=numberPplPurchase+1;
                itemDB.whereEqualTo("itemId",itemId).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot item:task.getResult()){
                                        Map<String, Integer> updateScore=new HashMap<>();
                                        updateScore.put("review",totalScore);
                                        updateScore.put("pplReview",numberPplReview);
                                        updateScore.put("purchase",numberPplPurchase);
                                        itemDB.document(item.getId()).set(updateScore, SetOptions.merge());
                                        Toast.makeText(item_detail.this,"You have purchase the item"
                                        ,Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(item_detail.this
                                                , MainScreen.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            }
                        });
            }
        });
    }

    private void addToCart() {
        userCart.document(ownerId).update("itemId",FieldValue.arrayUnion(itemId)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(item_detail.this, "Add to Cart success", Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, String> updateScore=new HashMap<>();
                    updateScore.put("itemId",itemId);
                    userCart.document(ownerId).set(updateScore);
                    Toast.makeText(item_detail.this, "Add to Cart success", Toast.LENGTH_SHORT).show();
                    userCart.document(ownerId).update("itemId",FieldValue.arrayUnion(itemId));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void removeItemWishList() {
        userWishList.document(ownerId).update("itemId", FieldValue.arrayRemove(itemId));
    }

    private void addToWishList() {
        userWishList.document(ownerId).update("itemId",FieldValue.arrayUnion(itemId)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(item_detail.this, "Add to WishList success", Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, String> updateScore=new HashMap<>();
                    updateScore.put("itemId",itemId);
                    userWishList.document(ownerId).set(updateScore);
                    Toast.makeText(item_detail.this, "Add to WishList success", Toast.LENGTH_SHORT).show();
                    userWishList.document(ownerId).update("itemId",FieldValue.arrayUnion(itemId));
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}