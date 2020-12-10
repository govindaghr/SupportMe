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

public class HomeFragment extends Fragment {
    private RecyclerView postList;

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef;

    String currentUserID;

    private HomeViewModel mViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.home_fragment, container, false);

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
        DisplayAllUsersPosts();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

    private void DisplayAllUsersPosts()
    {
        Query SortPostsInDecendingOrder = PostsRef.orderByChild("counter");

        FirebaseRecyclerOptions<Posts> options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(SortPostsInDecendingOrder, Posts.class).build();
        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options)
                {
                    @NonNull
                    @Override
                    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_posts, viewGroup, false);
                        return new PostsViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull final PostsViewHolder holder, int position, @NonNull Posts model)
                    {
                        final String PostKey = getRef(position).getKey();
                        holder.username.setText("@"+model.getUsername());
                        holder.time.setText("  " +model.getTime());
                        holder.date.setText("  " +model.getDate());
                        //holder.description.setText(model.getDescription());
                        holder.title.setText(model.getTitle());
                        Picasso.get().load(model.getPostimage()).into(holder.postImage);

                       holder.mView.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick (View view)
                            {
                                Intent readStoryIntent = new Intent(view.getContext(), ReadStoryActivity.class);
                                readStoryIntent.putExtra("PostKey", PostKey);
                                startActivity(readStoryIntent);
                            }
                        });
                    }
                };

        postList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        TextView username, date, time, title, description;
        ImageView postImage;
        View mView;

        String currentUserId;

        public PostsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView = itemView;

            currentUserId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            username=itemView.findViewById(R.id.userid);
            date=itemView.findViewById(R.id.postDate);
            time=itemView.findViewById(R.id.postTime);
            //description=itemView.findViewById(R.id.post_description);
            title=itemView.findViewById(R.id.title);
            postImage=itemView.findViewById(R.id.postImage);
        }
    }

}