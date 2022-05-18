package com.hungphamcom.mellowcourse.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.hungphamcom.mellowcourse.R;
import com.hungphamcom.mellowcourse.adapter.shopItemRecyclerAdapter;
import com.hungphamcom.mellowcourse.model.Item;
import com.hungphamcom.mellowcourse.ui.item_detail;
import com.hungphamcom.mellowcourse.util.UserApi;

import java.util.ArrayList;
import java.util.List;

public class WishlistFragment extends Fragment {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private List<Item> itemList;
    private RecyclerView recyclerView;
    private shopItemRecyclerAdapter shopItemRecyclerAdapter;
    private shopItemRecyclerAdapter.RecyclerViewClickListener listener;

    private ArrayList<String> list;

    private CollectionReference collectionReference=db.collection("Item");
    private TextView noItemInShop;

    private CollectionReference userWishList = db.collection("WishList");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wishlist, container, false);

        firebaseAuth= FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();

        noItemInShop=rootView.findViewById(R.id.noWishListItemInShop);
        itemList=new ArrayList<>();

        recyclerView=rootView.findViewById(R.id.recyclerview_wishList_fragment);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getWishItem();


        return rootView;
    }

    private void getWishItem() {
        String userid= UserApi.getInstance().getUserId();
        Log.d("WishList", "getUID: "+userid);
        userWishList.document(userid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document=task.getResult();
                            if(document.exists()){
                                list=(ArrayList<String>) document.get("itemId");
                                if(list.size()==0){
                                    noItemInShop.setVisibility(View.VISIBLE);
                                    return;
                                }else {
                                    noItemInShop.setVisibility(View.INVISIBLE);
                                }
                                for(int i=0;i<list.size();i++){
                                    String x= list.get(i);
                                    collectionReference.whereEqualTo("itemId",x)
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
                                }
                            }
                        }
                    }
                });
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