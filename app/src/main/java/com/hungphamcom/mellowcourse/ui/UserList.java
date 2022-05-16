package com.hungphamcom.mellowcourse.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hungphamcom.mellowcourse.MainScreen;
import com.hungphamcom.mellowcourse.R;
import com.hungphamcom.mellowcourse.adapter.shopItemRecyclerAdapter;
import com.hungphamcom.mellowcourse.add_new_item;
import com.hungphamcom.mellowcourse.model.Item;
import com.hungphamcom.mellowcourse.util.UserApi;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class UserList extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<Item> itemList;
    private RecyclerView recyclerView;
    private com.hungphamcom.mellowcourse.adapter.shopItemRecyclerAdapter shopItemRecyclerAdapter;
    private shopItemRecyclerAdapter.RecyclerViewClickListener listener;

    private Uri imageURI;

    private CollectionReference collectionReference=db.collection("Item");
    private TextView noItemInShop;

    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        storageReference = FirebaseStorage.getInstance().getReference();

        recyclerView=findViewById(R.id.recyclerview_my_list);
        noItemInShop=findViewById(R.id.noItemInMyList);

        itemList=new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(UserList.this));

        collectionReference.whereEqualTo("userId", UserApi.getInstance().getUserId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            for(QueryDocumentSnapshot items : queryDocumentSnapshots){
                                Item item = items.toObject(Item.class);
                                itemList.add(item);
                            }
                            setOnClickListener();
                            shopItemRecyclerAdapter=new shopItemRecyclerAdapter(UserList.this
                            ,itemList,listener);
                            recyclerView.setAdapter(shopItemRecyclerAdapter);
                            shopItemRecyclerAdapter.notifyDataSetChanged();
                        }else {
                            noItemInShop.setVisibility(View.VISIBLE);
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
                builder=new AlertDialog.Builder(UserList.this);
                inflater= LayoutInflater.from(UserList.this);
                View view=inflater.inflate(R.layout.popup_my_list,null);

                Button delete=view.findViewById(R.id.delete_pop_up_option_user_list);
                Button edit=view.findViewById(R.id.edit_pop_up_option_user_list);
                Button check=view.findViewById(R.id.check_pop_up_option_user_list);

                builder.setView(view);
                dialog=builder.create();
                dialog.show();

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        collectionReference.whereEqualTo("itemId",itemList.get(position)
                        .getItemId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for(QueryDocumentSnapshot item:task.getResult()){
                                        collectionReference.document(item.getId()).delete();
                                    }
                                    Intent intent=new Intent(UserList.this
                                            , UserList.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri image=Uri.parse(itemList.get(position).getImageUrl());
                        Intent intent1=new Intent(UserList.this, add_new_item.class);
                        intent1.putExtra("itemName", itemList.get(position).getName());
                        intent1.putExtra("itemPrice", itemList.get(position).getPrice());
                        intent1.putExtra("itemDescription", itemList.get(position).getDescription());
                        intent1.putExtra("image", itemList.get(position).getImageUrl());
                        intent1.putExtra("timeAdd", itemList.get(position).getTimeAdded().getSeconds());
                        intent1.putExtra("itemId",itemList.get(position).getItemId());

                        startActivity(intent1);
                    }
                });

                check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(UserList.this, item_detail.class);
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
                });
            }
        };

    }
}