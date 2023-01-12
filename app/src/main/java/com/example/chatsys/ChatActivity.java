package com.example.chatsys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class ChatActivity extends AppCompatActivity {


    EditText et;
    ListView listView;

    ArrayList<MessageItem> messageItems = new ArrayList<>();
    ChatAdapter adapter;

    //Firebase Database 관리 객체참조변수
    FirebaseDatabase firebaseDatabase;

    //'chat'노드의 참조객체 참조변수
    DatabaseReference chatRef;

    private QrActivity qA;
    private ListView chat_view;
    private String viewNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //QrActivity에서 textViewNumber가져옴
        viewNumber = G.qrNumber;
        /*
        qA = (QrActivity) getApplicationContext();
        TextView mGetTextView = qA.textViewNumber;
        String viewNumber = qA.textViewNumber.getText().toString();
        */

        //채팅방 이름 설정
        getSupportActionBar().setTitle(viewNumber);

        et = findViewById(R.id.et);
        listView = findViewById(R.id.listview);
        adapter = new ChatAdapter(messageItems, getLayoutInflater());
        listView.setAdapter(adapter);
        /*
        Button clickSend = (Button) findViewById(R.id.clickSend);
        clickSend.setOnClickListener(new Button.OnClickListener(){
            @Override

        //****************onclick이면 여기로********************

        });
        */

        //Firebse DB관리 객체와 'chat'노드 참조객체 얻어오기
        firebaseDatabase = FirebaseDatabase.getInstance();
        chatRef = firebaseDatabase.getReference("chat");

        //firebseDB에서 채팅 메세지 실시간 읽어오기
        //'chat'노드에 저장되어 있는 데이터 읽어오기
        //chatRef에 데이터가 변경되는 것을 듣는 리스너 추가

        openChat((viewNumber));

    }

    public void clickSend(View view) {

        //firebase DB에 저장할 값들(닉네임, 메세지, 프로필이미지 url, 시간)
        String nickName = G.nickName;
        String message = et.getText().toString();
        String pofileUrl = G.porfileUrl;

        //메세지 작성시간을 문자열에 저장
        Calendar calendar = Calendar.getInstance(); // 현재시간에 대한 객체
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

        //firebase BD에 저장할 값(MessageItem객체) 설정
        MessageItem messageItem = new MessageItem(nickName, message, time, pofileUrl);
        chatRef.child("chat").child(viewNumber).push().setValue(messageItem);

        //EditText에 있는 글씨 지우기
        et.setText("");

        //소프트키패드를 안보이도록
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        //처음 시작할때 EditText가 다른 뷰들보다 우선시되어 포커스 받아서 소프트 키패드가 올라옴
    }

    //add, remove message
    /*
    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        MessageItem messageItem = dataSnapshot.getValue(MessageItem.class);
        adapter.add(messageItem.getName() + " : " + messageItem.getMessage());
    }
    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
        MessageItem messageItem = dataSnapshot.getValue(MessageItem.class);
        adapter.remove(messageItem.getName() + " : " + messageItem.getMessage());
    }
    */

    //채팅방 리스트??
    /*
    private void openChat(String textViewNumber) {
        // 리스트 어댑터 생성 및 세팅
        final ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        chat_view.setAdapter(adapter);

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
        databaseReference.child("chat").child(textViewNumber).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                addMessage(dataSnapshot, adapter);
                Log.e("LOG", "s:" + s);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                removeMessage(dataSnapshot, adapter);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    */

    private void openChat(String textViewNumber){
        final ArrayAdapter<String> adapter
                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        //chat_view.setAdapter(adapter);

        chatRef.child("chat").child(textViewNumber).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //새로 추가된 데이터(값: MessageItem 객체)가져오기
                MessageItem messageItem = dataSnapshot.getValue(MessageItem.class);

                //새로운 메세지를 리스트뷰에 추가하기 위해 ArrayList에 추가
                messageItems.add(messageItem);

                //리스트뷰 갱신
                adapter.notifyDataSetChanged();
                listView.setSelection(messageItems.size() - 1); //리스트뷰 마지막 위치로 스크롤 위치이동
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, FuncActivity.class);
        startActivity(intent);
        finish();
    }

}