package com.hungphamcom.mellowcourse.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.StorageReference;
import com.hungphamcom.mellowcourse.MainScreen;
import com.hungphamcom.mellowcourse.R;
import com.hungphamcom.mellowcourse.adapter.shopItemRecyclerAdapter;
import com.hungphamcom.mellowcourse.model.Item;
import com.hungphamcom.mellowcourse.ui.item_detail;
import com.hungphamcom.mellowcourse.util.UserApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<Item> itemList;
    private RecyclerView recyclerView;
    private shopItemRecyclerAdapter shopItemRecyclerAdapter;
    private shopItemRecyclerAdapter.RecyclerViewClickListener listener;

    private DatabaseReference itemLive;
    private CollectionReference collectionReference=db.collection("Item");
    private TextView noItemInShop;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_shop, container, false);

        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        noItemInShop=rootView.findViewById(R.id.noItemInShop);
        itemList=new ArrayList<>();

        itemLive= FirebaseDatabase.getInstance().getReference().child("Item");

        recyclerView=rootView.findViewById(R.id.recyclerview_shop_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        collectionReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot items : queryDocumentSnapshots) {
                                Item item = items.toObject(Item.class);
                                itemList.add(item);
                            }
                            setOnClickListener();
                            shopItemRecyclerAdapter = new shopItemRecyclerAdapter(getActivity(),
                                    itemList,listener);
                            recyclerView.setAdapter(shopItemRecyclerAdapter);
                            shopItemRecyclerAdapter.notifyDataSetChanged();


                        }else{
                            noItemInShop.setVisibility(View.VISIBLE);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return rootView;
    }

    private void setOnClickListener() {
        listener=new shopItemRecyclerAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent=new Intent(getActivity(), item_detail.class);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}