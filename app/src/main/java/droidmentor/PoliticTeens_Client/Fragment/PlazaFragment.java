package droidmentor.PoliticTeens_Client.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;

import droidmentor.PoliticTeens_Client.PostDetailActivity;
import droidmentor.PoliticTeens_Client.R;
import droidmentor.PoliticTeens_Client.models.Post;
import droidmentor.PoliticTeens_Client.viewholder.PostViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlazaFragment extends Fragment {

    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<Post, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    public TextView hot_list;
    public TextView new_list;
    Query postsQuery;

    public PlazaFragment() {}


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Log.d("3","tq");
        View rootView = inflater.inflate(R.layout.fragment_plaza, container, false);

        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]

        hot_list = (TextView) rootView.findViewById(R.id.plaza_hot);
        new_list = (TextView) rootView.findViewById(R.id.plaza_new);
        mRecycler = (RecyclerView) rootView.findViewById(R.id.plaza_list);
        mRecycler.setHasFixedSize(true);

        postsQuery = getQuery1(mDatabase);



        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up Layout Manager, reverse layout
        mManager = new LinearLayoutManager(getActivity());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);
        mRecycler.setAdapter(setAdapter_own(mAdapter));
        // Set up FirebaseRecyclerAdapter with the Query


    }

    @Override
    public void onResume() {
        super.onResume();

        hot_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postsQuery = getQuery2(mDatabase);
                mRecycler.setAdapter(setAdapter_own(mAdapter));
                Log.d("ㅅㅂ 핫","ㄴㅇ");
                Log.d("ㅅㅂ 핫","ㄴㅇ");
            }
        });
        new_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postsQuery = getQuery1(mDatabase);
                mRecycler.setAdapter(setAdapter_own(mAdapter));
                Log.d("ㅅㅂ 뉴","ㄴㅇ");
                Log.d("ㅅㅂ 뉴","ㄴㅇ");
            }
        });
    }

    public FirebaseRecyclerAdapter<Post, PostViewHolder> setAdapter_own(FirebaseRecyclerAdapter<Post, PostViewHolder> adapter){
        adapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class, R.layout.plaza_item_post,
                PostViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, final Post model, final int position) {
                final DatabaseReference postRef = getRef(position);

                // Set click listener for the whole post view
                final String postKey = postRef.getKey();
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postKey);
                        startActivity(intent);
                    }
                });

                // Determine if the current user has liked this post and set UI accordingly
                if (model.stars.containsKey(getUid())) {
                    viewHolder.starView.setImageResource(R.drawable.bbb);
                } else {
                    viewHolder.starView.setImageResource(R.drawable.aaa);
                }

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("plaza_posts").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("plaza_user-posts").child(model.uid).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });
            }
        };
        return adapter;
    }

    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getUid())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getUid());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getUid(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
            }
        });
    }


    // [END post_stars_transaction]

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.cleanup();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public Query getQuery1(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("plaza_posts/")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }
    public Query getQuery2(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("plaza_posts/").orderByChild("starCount")
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }

}
