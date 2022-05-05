package com.hungphamcom.mellowcourse.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.WriteResult;
import com.hungphamcom.mellowcourse.MainActivity;
import com.hungphamcom.mellowcourse.R;
import com.hungphamcom.mellowcourse.util.UserApi;
import com.hungphamcom.mellowcourse.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseAuth account =FirebaseAuth.getInstance();


    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private StorageReference storageReference;
    private CollectionReference collectionReference=db.collection("Users");
    private DatabaseReference mDatabase;

    public static final String KEY_STATUS ="status";

    private String userid;

    private TextView signOutBtn;
    private TextView becomeSeller;
    private TextView yesBtn;
    private TextView noBtn;

    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signOutBtn=view.findViewById(R.id.sign_out_profile);
        becomeSeller=view.findViewById(R.id.become_seller_profile);

        userid=UserApi.getInstance().getUserId();

        signOutBtn.setOnClickListener(this);
        becomeSeller.setOnClickListener(this);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        Toast.makeText(getActivity(),UserApi.getInstance().getUserId(),Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_out_profile:
                account.signOut();
                Intent setupIntent = new Intent(getActivity(),MainActivity.class);
                Toast.makeText(getActivity(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(setupIntent);
                break;
            case R.id.become_seller_profile:
                popupcheck();
                break;
        }
    }

    private void popupcheck() {
        builder=new AlertDialog.Builder(getActivity());
        inflater=LayoutInflater.from(getActivity());
        View view=inflater.inflate(R.layout.popup_are_you_sure_option,null);
        final PopupWindow popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        TextView no=view.findViewById(R.id.no_pop_up_option);
        TextView yes=view.findViewById(R.id.yes_pop_up_option);

        popupWindow.showAsDropDown(view, 0, 0);


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                becomeASeller();
                popupWindow.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

    }

    private void becomeASeller() {
        collectionReference.whereEqualTo("userId",userid)
                    .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot users : queryDocumentSnapshots){
                            UserApi userApi= users.toObject(UserApi.class);
                            userApi.setStatus(Util.KEY_SELLER);
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