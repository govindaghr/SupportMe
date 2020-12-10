package com.lhayuel.supportme;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsViewHolder extends RecyclerView.ViewHolder {
    TextView myUserName, myComment, myDate, myTime;
    public CommentsViewHolder(@NonNull View itemView) {
        super(itemView);
        myUserName = itemView.findViewById(R.id.comment_username);
        myComment = itemView.findViewById(R.id.comment_text);
        myDate = itemView.findViewById(R.id.comment_date);
        myTime = itemView.findViewById(R.id.comment_time);
    }
}
