package com.example.chatsys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    EditText etName;
    CircleImageView ivProfile;
    String get_nickname;

    String G_qrNumber;

    Uri imgUri; //선택한 프로필 이미지 경로 URI

    //전에 접속한 적 있는지 확인하기 위한 boolean값
    boolean isFirst= true;
    boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName=findViewById(R.id.et_name);
        ivProfile=findViewById(R.id.iv_profile);

        //핸드폰에 저장되어있는 프로필 읽어오기
        loadData();
        if(G.nickName!=null){
            etName.setText(G.nickName);
            Picasso.get().load(G.porfileUrl).into(ivProfile);

            isFirst = false;
        }
    }

    //프로필 이미지 선택
    public void clickImage(View view){
        //프로필 이미지 선택하도록 Gallery 앱실행
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:
                if(resultCode==RESULT_OK){
                    imgUri= data.getData();
                    Picasso.get().load(imgUri).into(ivProfile);
                    //변경된 이미지 있음
                    isChanged=true;
                }
                break;
        }
    }



    public void clickBtn(View view){
                //바꾼거 없고 처음접속 아님
                if(!isChanged && !isFirst){
                    loadQR();
                    if(G.qrNumber != null){
                        Intent intent = new Intent(this,FuncActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(this, QrActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else{
                    //1. save 작업
                    saveData();
                }
        }

    void saveData(){
        //EditText의 닉네임 가져오기
        G.nickName=etName.getText().toString();

        //이미지를 선택하지 않은경우
        if(imgUri==null) return;

        //Firebase storage에 이미지 저장을 위한 파일명 만들기(날짜 기반)
        SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");
        String fileName = sdf.format(new Date()) + ".png";

        //Firebase storage에 저장
        FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
        final StorageReference imgRef= firebaseStorage.getReference("profileImages/"+fileName);

        //파일 업로드
        UploadTask uploadTask=imgRef.putFile(imgUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                //이미지 업로드 성공
                //firebase storage의 이미지 파일 다운로드 URL을 얻어오기
                imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //파라미터로 firebase의 저장소에 저장되어 있는
                        //이미지에 대한 다운로드 주소(URL)을 문자열로 얻어오기
                        G.porfileUrl= uri.toString();
                        Toast.makeText(MainActivity.this, "프로필 저장 완료", Toast.LENGTH_SHORT).show();

                        //1. Firebase Database에 nickName, profileUrl을 저장
                        //firebase DB관리자 객체 소환
                        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                        //'profiles'라는 이름의 자식 노드 참조 객체 얻어오기
                        DatabaseReference profileRef= firebaseDatabase.getReference("profiles");

                        //닉네임을 key 식별자로 하고 프로필 이미지의 주소를 값으로 저장
                        profileRef.child(G.nickName).setValue(G.porfileUrl);

                        //2. 내 phone에 nickName, profileUrl을 저장
                        SharedPreferences preferences= getSharedPreferences("account",MODE_PRIVATE);
                        SharedPreferences.Editor editor=preferences.edit();

                        editor.putString("nickName",G.nickName);
                        editor.putString("profileUrl", G.porfileUrl);

                        editor.commit();
                        //저장이 완료되었으니 QrActivity로 전환

                        if(G.qrNumber != null){
                            Intent intent=new Intent(MainActivity.this, FuncActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(MainActivity.this, QrActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                });
            }
        });
    }   //saveDate()...

    void loadData(){
        SharedPreferences preferences=getSharedPreferences("account",MODE_PRIVATE);
        G.nickName=preferences.getString("nickName", null);
        G.porfileUrl=preferences.getString("profileUrl", null);
    }
    void loadQR(){
        SharedPreferences preferences=getSharedPreferences("qrNumber_inPhone",MODE_PRIVATE);
        G.qrNumber=preferences.getString("qrnumber", null);
    }

}