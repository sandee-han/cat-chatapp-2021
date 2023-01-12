package com.example.chatsys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static java.security.AccessController.getContext;

public class QrActivity extends AppCompatActivity {
    //view Objects
    private Button buttonScan;
    public TextView textViewNumber, textViewResult;
    String[] chatNumStr = { "1", "2", "3", "4" };


    public static Context context_qr;
    String viewNumber;
    String G_qrNumber;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        context_qr = this;
        //View Objects
        buttonScan = (Button) findViewById(R.id.buttonScan);
        textViewNumber = (TextView) findViewById(R.id.textViewNumber);
        textViewResult = (TextView)  findViewById(R.id.textViewResult);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        //button onClick
        buttonScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //scan option
                qrScan.setPrompt("Scanning...");
                //qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });

    }


    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        /*
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        final StorageReference qrRef= firebaseStorage.getReference("qrNumber");
        UploadTask uploadTask=qrRef.putString(G.qrNumber);
        */

        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference profileRef= firebaseDatabase.getReference("qrNumber");


            if (result != null) {
                //qrcode 가 없으면
                if (result.getContents() == null) {
                    Toast.makeText(QrActivity.this, "취소!", Toast.LENGTH_SHORT).show();
                } else {
                    //qrcode 결과가 있으면
                    Toast.makeText(QrActivity.this, "스캔완료!", Toast.LENGTH_SHORT).show();
                    try {
                        //data를 json으로 변환
                        JSONObject obj = new JSONObject(result.getContents());
                        textViewNumber.setText(obj.getString("qrnumber"));
                        //textViewNumber.setText(result.getContents());
                        //viewNumber = textViewNumber.toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Toast.makeText(MainActivity.this, result.getContents(), Toast.LENGTH_LONG).show();
                        textViewResult.setText(result.getContents());
                    }
                }
                //viewNumber에 textViewNumber JSON의 qrnumber값 String으로 입력
                viewNumber = textViewNumber.getText().toString();

                //받은 qr값 G.java의 G.qrNumber에 저장
                profileRef.child(viewNumber).setValue(viewNumber);
                SharedPreferences preferences = getSharedPreferences("qrNumber_inPhone", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("qrnumber", viewNumber);
                G.qrNumber=viewNumber;
                editor.commit();

                if (Arrays.asList(chatNumStr).contains(viewNumber)) {
                    Intent intent = new Intent(this, FuncActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }


    }





}
