package com.lhayuel.supportme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

public class ReadStoryActivity extends AppCompatActivity {
    private ActionBar mActionbar;
    private ImageView postImage;
    private TextView title, postTime, postDate, userid, post_description;

    private RecyclerView CommentsList;
    private Button post_comment;
    private EditText etComment;

    private DatabaseReference ClickPostRef, UsersRef, PostsRef;
    private FirebaseAuth mAuth;

    private String PostKey, currentUserID, databaseUserID, postTitle, description, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_story);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        PostKey = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("PostKey")).toString();
        ClickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey).child("Comments");

        mActionbar = getSupportActionBar();
        assert mActionbar != null;
        mActionbar.setDisplayShowHomeEnabled(true);
        //mActionbar.setTitle("Add Post");

        postImage = findViewById(R.id.postImage);
        title = findViewById(R.id.title);
        postTime = findViewById(R.id.postTime);
        postDate = findViewById(R.id.postDate);
        userid = findViewById(R.id.userid);
        post_description = findViewById(R.id.post_description);

        //Comments
        CommentsList = (RecyclerView) findViewById(R.id.comments_list);
        CommentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true); //shows last comment on top
        //linearLayoutManager.setStackFromEnd(true);
        CommentsList.setLayoutManager(linearLayoutManager);
        etComment = (EditText) findViewById(R.id.comment_input);


        ClickPostRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    description = Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString();
                    image = Objects.requireNonNull(dataSnapshot.child("postimage").getValue()).toString();
                    databaseUserID = Objects.requireNonNull(dataSnapshot.child("uid").getValue()).toString();
                    postTitle = Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString();
                    databaseUserID = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();

                    title.setText(postTitle);
                    userid.setText("@" +databaseUserID);
                    post_description.setText(description);
                    Picasso.get().load(image).into(postImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });


        FirebaseRecyclerOptions<Comments> options=new FirebaseRecyclerOptions.Builder<Comments>().setQuery(PostsRef,Comments.class).build();
        FirebaseRecyclerAdapter<Comments, CommentsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull CommentsViewHolder holder, int position, @NonNull Comments model)
            {
                holder.myUserName.setText("@" + model.getUsername() + "   ");
                holder.myComment.setText(model.getComment());
                holder.myDate.setText(model.getDate());
                holder.myTime.setText("  " + model.getTime());
            }

            @NonNull
            @Override
            public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_comments,viewGroup,false);

                return new CommentsViewHolder(view);
            }
        };

        CommentsList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public void postComment(View view) {
        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String userName = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();

                    ValidateComment(userName);

                    etComment.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }


    @Override
    protected void onStart()
    {
        super.onStart();
    }


    private void ValidateComment(String userName)
    {
        String commentText = etComment.getText().toString();

        if(TextUtils.isEmpty(commentText))
        {
            Toast.makeText(this, "Please write something to add comment", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calFordDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            final String saveCurrentDate = currentDate.format(calFordDate.getTime());

            //Calendar calFordTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
            final String saveCurrentTime = currentTime.format(calFordDate.getTime());

            final String RandomKey = currentUserID + saveCurrentDate + saveCurrentTime;

            HashMap<String, Object> commentsMap = new HashMap<>();
            commentsMap.put("uid", currentUserID);
            commentsMap.put("comment", commentText);
            commentsMap.put("date", saveCurrentDate);
            commentsMap.put("time", saveCurrentTime);
            commentsMap.put("username", userName);

            PostsRef.child(RandomKey).updateChildren(commentsMap)
                    .addOnCompleteListener(new OnCompleteListener()
                    {
                        @Override
                        public void onComplete(@NonNull Task task)
                        {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ReadStoryActivity.this, "Commented Added successfully", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(ReadStoryActivity.this, "Error Occured, try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}