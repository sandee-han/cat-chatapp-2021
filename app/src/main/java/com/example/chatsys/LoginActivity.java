package com.example.chatsys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private EditText mEmail;
    private EditText mPassword;

    SharedPreferences sp;

    @Override
    protected void onDestroy() {
        super.onDestroy();

        save(mEmail.getText().toString());
    }

    public void save(String s){
        sp = getSharedPreferences("sp",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("save",s);
        editor.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.login_email);
        mPassword = findViewById(R.id.login_password);
        findViewById(R.id.login_signup).setOnClickListener(this);
        findViewById(R.id.login_success).setOnClickListener(this);

        sp = getSharedPreferences("sp", MODE_PRIVATE);
        String save = sp.getString("save", "");
        mEmail.setText(save);


        if(G.loginID!=null){
            mEmail.setText(G.loginID); //chu2
        }
    }



    @Override
    public void onClick(View v) {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference profileRef= firebaseDatabase.getReference("loginID");
        switch (v.getId()) {
            case R.id.login_signup:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.login_success:
                mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        Toast.makeText(com.example.chatsys.LoginActivity.this, "login success",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(com.example.chatsys.LoginActivity.this, MainActivity.class));
                                    }
                                } else {

                                    Toast.makeText(com.example.chatsys.LoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                G.loginID = mEmail.getText().toString();//chu2
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}