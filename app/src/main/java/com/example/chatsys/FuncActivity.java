package com.example.chatsys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;

public class FuncActivity extends AppCompatActivity {

    private Button enterBB;
    private Button enterChat;
    private Button scanother;

    private String viewNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_func);

        enterBB = (Button) findViewById(R.id.enterBB);
        enterChat = (Button) findViewById(R.id.enterChat);
        scanother = (Button) findViewById(R.id.scanother);

        //QrActivity에서 textViewNumber가져옴
        viewNumber = G.qrNumber;

        getSupportActionBar().setTitle(viewNumber);

        //button onClick
        enterChat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(FuncActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });

        enterBB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BBActivity.class);
                startActivity(intent);
                finish();
            }
        });

        scanother.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), QrActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Light_Dialog);
        builder.setMessage("로그아웃 하시겠습니까?")
            .setTitle("로그아웃")
                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Dialog", "취소");
                        Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                    }
                })
                .setNeutralButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(FuncActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setCancelable(true)
                .show();
    }


}
