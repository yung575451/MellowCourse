package com.hungphamcom.mellowcourse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hungphamcom.mellowcourse.model.U_Status;
import com.hungphamcom.mellowcourse.util.UserApi;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private ProgressBar progressBar;

    private CardView loginButton;
    private ImageView changePassword;

    private TextView forgetPasswordButton;
    private TextView createAccount;


    private String savePassword;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference userCollectionReference =db.collection("Users");
    private CollectionReference statusCollectionReference =db.collection("U_status");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth= FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar_login);

        setContentView(R.layout.activity_main);
        email=findViewById(R.id.email_login);
        createAccount=findViewById(R.id.registerNow_login);
        changePassword=findViewById(R.id.change_input_password_login);
        password=findViewById(R.id.password_login);
        progressBar=findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.INVISIBLE);

        loginButton=findViewById(R.id.login_login);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CreateAccountActivity.class));
                finish();
            }
        });
        changePassword.setOnClickListener(changeInput);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEmailPasswordUsers(email.getText().toString().trim()
                        ,password.getText().toString().trim());
            }
        });


    }

    private void loginEmailPasswordUsers(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);

        if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)){
            firebaseAuth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user=firebaseAuth.getCurrentUser();
                            assert user!=null;
                            String currentUserId=user.getUid();

                            userCollectionReference
                                    .whereEqualTo("userId",currentUserId)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                            if(error!=null){
                                                return;
                                            }
                                            assert value!=null;
                                            if(!value.isEmpty()){
                                                progressBar.setVisibility(View.INVISIBLE);
                                                for(QueryDocumentSnapshot snapshot:value){
                                                    UserApi userApi=UserApi.getInstance();
                                                    userApi.setUsername(snapshot.getString("username"));
                                                    userApi.setUserId(currentUserId);
                                                    statusCollectionReference.whereEqualTo("userId",UserApi.getInstance().getUserId()).get()
                                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                                @Override
                                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                                    if(!queryDocumentSnapshots.isEmpty()){
                                                                        for(QueryDocumentSnapshot user: queryDocumentSnapshots){
                                                                            U_Status u_status=user.toObject(U_Status.class);
                                                                            userApi.setStatus(u_status.getStatus());
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                    startActivity(new Intent(MainActivity.this,MainScreen.class));
                                                }
                                            }
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
//                    Toast.makeText(MainActivity.this
//                            ,"Please enter email and password"
//                            ,Toast.LENGTH_LONG).show();
                }
            });
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this,"Please enter email and password"
                    ,Toast.LENGTH_LONG).show();
        }
    }


    View.OnClickListener changeInput=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int check=password.getInputType();

            if(password.getInputType()== 129){
                Log.d("main", "onClick: "+password.getInputType());
                password.setInputType(144);
                Picasso.get().load(R.drawable.ic_akar_icons_eye_slashed_login)
                        .placeholder(R.drawable.ic_akar_icons_eye_open_login)
                        .fit()
                        .into(changePassword);
            }else {
                Log.d("main", "onClick: "+password.getInputType());
                password.setInputType(129);
                Picasso.get().load(R.drawable.ic_akar_icons_eye_open_login)
                        .placeholder(R.drawable.ic_akar_icons_eye_slashed_login)
                        .fit()
                        .into(changePassword);
            }
        }
    };
}