package com.lhayuel.supportme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ReadMyStoryActivity extends AppCompatActivity {
    private ActionBar mActionbar;
    private ImageView postImage;
    private TextView title, postTime, postDate, userid, post_description;
    private RecyclerView CommentsList;

    private DatabaseReference ClickPostRef, UsersRef, PostsRef;
    private FirebaseAuth mAuth;

    private String PostKey, currentUserID, databaseUserID, postTitle, description, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_my_story);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        PostKey = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("PostKey")).toString();
        ClickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey).child("Comments");

        /*mActionbar = getSupportActionBar();
        if(mActionbar != null){
            mActionbar.setDisplayHomeAsUpEnabled(true);
            mActionbar.setDisplayHomeAsUpEnabled(true);
        }*/

        postImage = findViewById(R.id.postImage);
        title = findViewById(R.id.title);
        postTime = findViewById(R.id.postTime);
        postDate = findViewById(R.id.postDate);
        userid = findViewById(R.id.userid);
        post_description = findViewById(R.id.post_description);

        //Comments
        CommentsList = (RecyclerView) findViewById(R.id.comments_list);
        //CommentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ReadMyStoryActivity.this);
        linearLayoutManager.setReverseLayout(true); //shows last comment on top
        linearLayoutManager.setStackFromEnd(true);
        CommentsList.setLayoutManager(linearLayoutManager);


        ClickPostRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    description = Objects.requireNonNull(dataSnapshot.child("description").getValue()).toString();
                    image = Objects.requireNonNull(dataSnapshot.child("postimage").getValue()).toString();
                    //databaseUserID = Objects.requireNonNull(dataSnapshot.child("uid").getValue()).toString();
                    postTitle = Objects.requireNonNull(dataSnapshot.child("title").getValue()).toString();
                    databaseUserID = Objects.requireNonNull(dataSnapshot.child("username").getValue()).toString();

                    title.setText(postTitle);
                    userid.setText(getString(R.string.at_the_rate) + databaseUserID);
                    post_description.setText(description);
                    Picasso.get().load(image).into(postImage);

                    /*if(currentUserID.equals(databaseUserID))
                    {
                        DeletePostButton.setVisibility(View.VISIBLE);
                        EditPostButton.setVisibility(View.VISIBLE);
                    }
                    EditPostButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            EditCurrentPost(description);
                        }
                    });*/
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void initData(){
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.story_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() ==android.R.id.home){
            onBackPressed();
        }
        if (item.getItemId() == R.id.edit_story) {/*Intent editIntent = new Intent(ReadMyStoryActivity.this, EditStoryActivity.class);
                editIntent.putExtra("PostKey", PostKey);
                startActivity(editIntent);*/
            editCurrentPost(description);
        }
       if (item.getItemId() == R.id.delete_story){
           final AlertDialog.Builder confirmDelete = new AlertDialog.Builder(ReadMyStoryActivity.this);
           confirmDelete.setTitle("Do you want to delete?");
           confirmDelete.setMessage("Your story will be deleted permanently and you'll not be able to retrieve it later");
           confirmDelete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   deletePost();
               }
           });

           confirmDelete.setNegativeButton("No", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
               }
           });
           confirmDelete.setCancelable(false);
           confirmDelete.show();
       }
        return true;
    }

    private void deletePost(){
        ClickPostRef.removeValue();
        Intent intent = new Intent(ReadMyStoryActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Post has been deleted.", Toast.LENGTH_SHORT).show();
    }


    private void editCurrentPost (String description)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReadMyStoryActivity.this);
        builder.setTitle("Edit Post: ");

        final EditText inputField = new EditText(ReadMyStoryActivity.this);
        inputField.setText(description);
        builder.setView(inputField);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                ClickPostRef.child("description").setValue(inputField.getText().toString());
                Toast.makeText(ReadMyStoryActivity.this, "Post Updated successfully", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
            }
        });

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
        //Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.color.colorLightBlueBackground);
    }
}