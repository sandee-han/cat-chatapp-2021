package com.example.chatsys.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatsys.R;
import com.example.chatsys.models.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<com.example.chatsys.adapters.PostAdapter.PostViewHolder> {

    private List<Post> datas;

    public PostAdapter(List<Post> datas) {
        this.datas = datas;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post data = datas.get(position);
        holder.nickname.setText("작성자: " + data.getNickname());
        holder.title.setText(data.getTitle());
        holder.contents.setText(data.getContents());
    }

    @Override
    public int getItemCount() {
        if (datas == null) return 0;
        return datas.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {

        private TextView nickname;
        private TextView title;
        private TextView contents;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            nickname = itemView.findViewById(R.id.item_post_nickname);
            title = itemView.findViewById(R.id.item_post_title);
            contents = itemView.findViewById(R.id.item_post_contents);
        }
    }
}
