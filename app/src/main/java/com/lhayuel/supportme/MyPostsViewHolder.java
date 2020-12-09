package com.lhayuel.supportme;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MyPostsViewHolder  extends RecyclerView.ViewHolder{
    TextView username, date, time, title, description;
    ImageView postImage;
    View mView;

    String currentUserId;

    public MyPostsViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;

        currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        username = itemView.findViewById(R.id.userid);
        date = itemView.findViewById(R.id.postDate);
        time = itemView.findViewById(R.id.postTime);
        //description=itemView.findViewById(R.id.post_description);
        title = itemView.findViewById(R.id.title);
        postImage = itemView.findViewById(R.id.postImage);
    }

    public void setUsername(String username)
    {
        TextView userName = (TextView) mView.findViewById(R.id.userid);
        userName.setText(username);
    }

    public void setTime(String time)
    {
        TextView PostTime = (TextView) mView.findViewById(R.id.postTime);
        PostTime.setText("   " + time);
    }

    public void setDate(String date)
    {
        TextView PostDate = (TextView) mView.findViewById(R.id.postDate);
        PostDate.setText("   " + date);
    }

    public void setTitle(String description)
    {
        TextView title = (TextView) mView.findViewById(R.id.title);
        title.setText(description);
    }

    public void setPostImage(String postImage)
    {
        ImageView PostImage = (ImageView) mView.findViewById(R.id.postImage);
        Picasso.get().load(postImage).into(PostImage);
    }
}
