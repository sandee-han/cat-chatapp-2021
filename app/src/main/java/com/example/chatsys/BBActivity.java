package com.example.chatsys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsys.adapters.PostAdapter;
import com.example.chatsys.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BBActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewItemClickListener.OnItemClickListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private RecyclerView mPostRecyclerView;
    private PostAdapter mAdapter;
    private List<Post> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bb);
        mPostRecyclerView = findViewById(R.id.main_recylerview);
        findViewById(R.id.main_post_edit).setOnClickListener(this);
        mPostRecyclerView.addOnItemTouchListener(new RecyclerViewItemClickListener(this, mPostRecyclerView, this));

        getSupportActionBar().setTitle(G.qrNumber);
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseID.post = G.qrNumber;
        mDatas = new ArrayList<>();
        mStore.collection(FirebaseID.post)
                .orderBy(FirebaseID.timestamp, Query.Direction.DESCENDING) //오름차순
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                        if (queryDocumentSnapshots != null) {
                            mDatas.clear();
                            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                                Map<String, Object> shot = snap.getData();
                                String documentId = String.valueOf(shot.get(FirebaseID.documentId));
                                String nickname = String.valueOf(shot.get(FirebaseID.nickname));
                                String title = String.valueOf(shot.get(FirebaseID.title));
                                String contents = String.valueOf(shot.get(FirebaseID.contents));
                                Post data = new Post(documentId, nickname, title, contents);
                                mDatas.add(data);
                            }
                            mAdapter = new PostAdapter(mDatas);
                            mPostRecyclerView.setAdapter(mAdapter);
                        }
                    }
                });
    }
    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, PostActivity.class));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, post2Activity.class);
        intent.putExtra(FirebaseID.documentId,mDatas.get(position).getDocumentID());
        startActivity(intent);

    }

    @Override
    public void onItemLongClick(View view, int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("삭제하시겠습니까?");
        dialog.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mStore.collection(FirebaseID.post).document(mDatas.get(position).getDocumentID()).delete();
                Toast.makeText(BBActivity.this, "삭제 되었습니다.", Toast.LENGTH_SHORT).show();

            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(BBActivity.this, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setTitle("삭제 알림");
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, FuncActivity.class);
        startActivity(intent);
        finish();
    }
}
