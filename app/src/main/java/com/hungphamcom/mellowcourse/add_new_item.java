package com.hungphamcom.mellowcourse;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hungphamcom.mellowcourse.model.Item;
import com.hungphamcom.mellowcourse.ui.UserList;
import com.hungphamcom.mellowcourse.util.UserApi;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class add_new_item extends AppCompatActivity implements View.OnClickListener {
    private ImageView homeLogo_add_item;
    private ImageView addSign_add_item;
    private ImageView searchSign;
    private ImageView shopCart_add_item;
    private ImageView notification_add_item;

    private ImageView addPhotoButton_add_item;
    private ImageView imageView;

    private ProgressBar progressBar;
    private String itemIdDetail;

    private String itemId;

    private TextView sellerName_add_item;
    private TextView itemName_add_item;
    private TextView itemPrice_add_item;
    private TextView itemDescription_add_item;

    private Button addItemBtn_add_item;
    private TextView backToMainScreen;

    private String timeAdd;

    private int checkImg;

    private String currentUserId;
    private String currentUserName;
    private ActivityResultLauncher<String> mTakePhoto;
    private Uri imageURI;
    private String image;

    private DatabaseReference itemLive;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //connect to fire store;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference collectionReference = db.collection("Item");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);

        checkImg=0;

        itemLive= FirebaseDatabase.getInstance().getReference().child("Item");

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        homeLogo_add_item = findViewById(R.id.logo_mainScreen);
        addSign_add_item = findViewById(R.id.addItemTaskBar_addItemScreen);

        addPhotoButton_add_item = findViewById(R.id.cameraButton_add_item_screen);
        imageView = findViewById(R.id.add_imageView_add_item_screen);

        progressBar = findViewById(R.id.progressBar_add_item);
        itemName_add_item = findViewById(R.id.item_name_add_item_screen);
        itemPrice_add_item = findViewById(R.id.item_price_add_item_screen);
        itemDescription_add_item = findViewById(R.id.item_description_add_item_screen);

        sellerName_add_item = findViewById(R.id.seller_name_add_item_screen);

        addItemBtn_add_item = findViewById(R.id.addItem_addItemScreen);
        backToMainScreen = findViewById(R.id.back_to_main_screen_addItemScreen);

        addPhotoButton_add_item.setOnClickListener(this);
        backToMainScreen.setOnClickListener(this);
        addItemBtn_add_item.setOnClickListener(this);

        mTakePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        imageView.setImageURI(result);
                        imageURI = result;
                        checkImg=1;
                    }
                });
        if (UserApi.getInstance() != null) {
            currentUserId = UserApi.getInstance().getUserId();
            currentUserName = UserApi.getInstance().getUsername();

            sellerName_add_item.setText(currentUserName);
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemName_add_item.setText(extras.getString("itemName"));
            itemDescription_add_item.setText(extras.getString("itemDescription"));
            itemPrice_add_item.setText(String.valueOf(extras.getInt("itemPrice")));
            image=extras.getString("image");
            timeAdd=extras.getString("timeAdd");
            itemIdDetail=extras.getString("itemId");

            imageURI= Uri.parse(image);
            Picasso.get().load(image)
                    .fit().into(imageView);

            addItemBtn_add_item.setText("Update Item");
        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addItem_addItemScreen:
                checkOpt();
                break;
            case R.id.cameraButton_add_item_screen:
                mTakePhoto.launch("image/*");
                imageView.setBackgroundColor(Color.parseColor("#292723"));
                addPhotoButton_add_item.setAlpha(0.5F);
                break;
            case R.id.back_to_main_screen_addItemScreen:
                finish();
                break;
        }
    }

    private void checkOpt() {
        String checkBtn=addItemBtn_add_item.getText().toString().toLowerCase(Locale.ROOT);
        switch (checkBtn){
            case "add item":
                addItem();
                Toast.makeText(this, "add item", Toast.LENGTH_SHORT).show();
                break;
            case "update Item":
                updateItem();
                break;
        }
    }

    private void updateItem() {
        String name = itemName_add_item.getText().toString().trim();
        int price = Integer.parseInt(itemPrice_add_item.getText().toString().trim());
        String description = itemDescription_add_item.getText().toString().trim();
        progressBar.setVisibility(View.VISIBLE);
        final StorageReference filepath = storageReference
                .child("item_images").child("my_image_" + timeAdd);

        final StorageReference imagesPath = storageReference
                .child("item_images").child("my_image_" + Timestamp.now().getSeconds());
        if(checkImg==0){
            HashMap<String,String> map=new HashMap<>();
            map.put("name",name);
            map.put("description",description);

            HashMap<String,Integer> map1=new HashMap<>();
            map1.put("price",price);

            HashMap hashMap=new HashMap();

            Item item=new Item();
            item.setName(name);
            item.setDescription(description);
            item.setPrice(price);

            hashMap.put("name",name);
            itemLive.child(itemIdDetail).updateChildren(hashMap);

            collectionReference.whereEqualTo("itemId",itemIdDetail)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        for(QueryDocumentSnapshot items: task.getResult()){
                            collectionReference.document(items.getId()).set(map, SetOptions.merge());
                            collectionReference.document(items.getId()).set(map1, SetOptions.merge());
                        }
                    }
                }
                    }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                    Intent intent=new Intent(add_new_item.this
                            , UserList.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }
            });
        }else {

            filepath.delete();

            if (!TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(name)
                    && !TextUtils.isEmpty(description)
                    && imageURI != null) {
                imagesPath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imagesPath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl=uri.toString();

                                HashMap<String,String> map=new HashMap<>();
                                map.put("name",name);
                                map.put("description",description);
                                map.put("imageUrl",imageUrl);

                                HashMap<String,Integer> map1=new HashMap<>();
                                map1.put("price",price);

                                collectionReference.whereEqualTo("itemId",itemIdDetail)
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()){
                                            for(QueryDocumentSnapshot items: task.getResult()){
                                                collectionReference.document(items.getId()).set(map, SetOptions.merge());
                                                collectionReference.document(items.getId()).set(map1, SetOptions.merge());
                                            }
                                        }
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        finish();
                                        Intent intent=new Intent(add_new_item.this
                                                , UserList.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }
    }

    private void addItemsToLiveDataBase(Item item) {
        itemLive.child(itemId).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressBar.setVisibility(View.INVISIBLE);
                startActivity(new Intent(add_new_item.this
                        , MainScreen.class));
                finish();
            }
        });
    }

    private void addItem() {
        String name = itemName_add_item.getText().toString().trim();
        int price = Integer.parseInt(itemPrice_add_item.getText().toString().trim());
        String description = itemDescription_add_item.getText().toString().trim();
        Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();

        progressBar.setVisibility(View.VISIBLE);
        final StorageReference filepath = storageReference
                .child("item_images").child("my_image_" + Timestamp.now().getSeconds());

        if (!TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(name)
                && !TextUtils.isEmpty(name)
                && imageURI != null) {
            filepath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            itemId=String.valueOf(Timestamp.now().getSeconds());

                            Item item = new Item();
                            item.setName(name);
                            item.setPrice(price);
                            item.setDescription(description);
                            item.setImageUrl(imageUrl);
                            item.setTimeAdded(new Timestamp(new Date()));
                            item.setItemId(itemId);
                            item.setPurchase(0);
                            item.setReview(0);
                            item.setUsername(currentUserName);
                            item.setUserId(currentUserId);
                            item.setPplReview(0);


                            Toast.makeText(add_new_item.this, "click", Toast.LENGTH_SHORT).show();

                            collectionReference.add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(add_new_item.this
                                            , MainScreen.class));
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("addNewItem", "onFailure: " + e.getMessage());
                                }
                            });

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d("addNewItem", "onFailure: " + e.getMessage());
                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Log.d("addNewItem", "addItem: " + "can't add");
        }
    }
}