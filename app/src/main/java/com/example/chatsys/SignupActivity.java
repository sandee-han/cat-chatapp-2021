package com.example.chatsys;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailText;
    private EditText mPasswordText;
    private EditText mNickname;
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mNickname =findViewById(R.id.sign_nickname);
        mEmailText= findViewById(R.id.sign_email);
        mPasswordText=findViewById(R.id.sign_password);

        findViewById(R.id.sign_success).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mAuth.createUserWithEmailAndPassword(mEmailText.getText().toString(), mPasswordText.getText().toString())
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user !=null) {
                                Toast.makeText(com.example.chatsys.SignupActivity.this, "Sign up success.",
                                        Toast.LENGTH_SHORT).show();
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put(FirebaseID.documentId, user.getUid());
                                userMap.put(FirebaseID.nickname,mNickname.getText().toString());
                                userMap.put(FirebaseID.email,mEmailText.getText().toString());
                                userMap.put(FirebaseID.password,mPasswordText.getText().toString());

                                mStore.collection(FirebaseID.user)
                                        .document(user.getUid())
                                        .set(userMap,SetOptions.merge());
                                finish();
                            }
                        } else {
                            Toast.makeText(com.example.chatsys.SignupActivity.this, "Sign up error.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}