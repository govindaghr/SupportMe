package com.lhayuel.supportme;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MyStoryFragment extends Fragment {
    private RecyclerView postList;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef;

    String currentUserID;

    private MyStoryViewModel mViewModel;

    public static MyStoryFragment newInstance() {
        return new MyStoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.my_story_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        postList = view.findViewById(R.id.recyclerView);
        postList.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);
        DisplayMyAllPosts();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyStoryViewModel.class);
        // TODO: Use the ViewModel
    }


    private void DisplayMyAllPosts()
    {
        //Query SortPostsInDecendingOrder = PostsRef.orderByChild("date");//orderByChild("counter")

        Query myPostsQuery = PostsRef.orderByChild("uid").startAt(currentUserID).endAt(currentUserID + "\uf8ff");

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(myPostsQuery, Posts.class)
                .build();

        FirebaseRecyclerAdapter<Posts, MyPostsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Posts, MyPostsViewHolder>(options) {
            @NonNull
            @Override
            public MyPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_posts, parent, false);
                return new MyPostsViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull MyPostsViewHolder holder, int position, @NonNull Posts model) {
                final String PostKey = getRef(position).getKey();

                holder.setUsername("@"+model.getUsername());
                holder.setTime(model.getTime());
                holder.setDate(model.getDate());
                holder.setPostImage(model.getPostimage());
                holder.setTitle(model.getTitle());


                 holder.mView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick (View view)
                            {
                                Intent viewStory = new Intent(view.getContext(), ReadMyStoryActivity.class);
                                viewStory.putExtra("PostKey", PostKey);
                                startActivity(viewStory);
                            }
                        });
            }
        };

        postList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

}