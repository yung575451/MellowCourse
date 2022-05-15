package com.hungphamcom.mellowcourse.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.StorageReference;
import com.hungphamcom.mellowcourse.MainActivity;
import com.hungphamcom.mellowcourse.MainScreen;
import com.hungphamcom.mellowcourse.R;
import com.hungphamcom.mellowcourse.ui.UserList;
import com.hungphamcom.mellowcourse.util.UserApi;
import com.hungphamcom.mellowcourse.util.Util;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseAuth account =FirebaseAuth.getInstance();


    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private StorageReference storageReference;
    private CollectionReference usersCollectionReference=db.collection("Users");
    private CollectionReference statusCollectionReference= db.collection("U_status");
    private DatabaseReference mDatabase;

    private TextView username;
    private ImageView becomeSellerIcon;

    private TextView myList;
    private ImageView myListIcon;

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
        username=view.findViewById(R.id.username_profile);
        becomeSellerIcon=view.findViewById(R.id.become_seller_icon);

        signOutBtn.setOnClickListener(this);
        becomeSeller.setOnClickListener(this);

        myList=view.findViewById(R.id.my_list_profile);
        myListIcon=view.findViewById(R.id.my_list_icon);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        username.setText(UserApi.getInstance().getUsername());

        if(UserApi.getInstance().getStatus().equals("seller")){
            becomeSeller.setVisibility(View.INVISIBLE);
            becomeSellerIcon.setVisibility(View.INVISIBLE);
            myList.setVisibility(View.VISIBLE);
            myListIcon.setVisibility(View.VISIBLE);
            myList.setOnClickListener(this);
        }


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
            case R.id.my_list_profile:
                Intent intent=new Intent(getActivity(), UserList.class);
                startActivity(intent);
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
                MainScreen activity=(MainScreen) getContext();
                activity.initPagerAdapter();
                activity.showAddItem();
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
        String update=Util.KEY_SELLER;
        statusCollectionReference.whereEqualTo("userId",UserApi.getInstance().getUserId())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot user: task.getResult()){
                        Map<String, String> updateStatus=new HashMap<>();
                        updateStatus.put(Util.KEY_STATUS,update);
                        statusCollectionReference.document(user.getId()).set(updateStatus, SetOptions.merge());
                        UserApi.getInstance().setStatus(Util.KEY_SELLER);
                    }
                }
            }
        });
    }



}