package droidmentor.PoliticTeens_Client.Fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.firebase.database.ValueEventListener;

import droidmentor.PoliticTeens_Client.GetUserId;
import droidmentor.PoliticTeens_Client.JungPostDetailActivity;
import droidmentor.PoliticTeens_Client.R;
import droidmentor.PoliticTeens_Client.S;
import droidmentor.PoliticTeens_Client.models.JungPost;
import droidmentor.PoliticTeens_Client.viewholder.JungPostViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyJungdangFragment extends Fragment {

    FragmentManager manager;  //Fragment를 관리하는 클래스의 참조변수
    FragmentTransaction tran;  //실제로 Fragment를 추가/삭제/재배치 하는 클래스의 참조변수
    private DatabaseReference mDatabase;
    // [END define_database_reference]

    private FirebaseRecyclerAdapter<JungPost, JungPostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    ImageView mydang_color;
    TextView mydang_name;
    TextView mydang_people;
    TextView mydang_ideology;
    GradientDrawable color;

    String searchId=null;

    String user_club;

    public MyJungdangFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_myjundangs, container, false);

        // View view = inflater.inflate(R.layout.fragment_myjundangs, container, false);

        manager = (FragmentManager) getFragmentManager();
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // [END create_database_reference]


        mydang_color = (ImageView)rootView.findViewById(R.id.myjungdang_color);
        mydang_name = (TextView)rootView.findViewById(R.id.mydang_name);
        mydang_people = (TextView)rootView.findViewById(R.id.mydang_people);
        mydang_ideology = (TextView)rootView.findViewById(R.id.mydang_ideology);
        color = (GradientDrawable) mydang_color.getBackground();


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    user_club = dataSnapshot.child("users").child(GetUserId.getUid()).child("club").getValue(Long.class).toString();
                    //mydang_color.setBackgroundColor(Color.parseColor("#" + dataSnapshot.child("club").child(user_club).child("just_color").getValue(String.class)));
                    color.setColor(Color.parseColor("#" + dataSnapshot.child("club").child(user_club).child("just_color").getValue(String.class)));
                    S.jungColor = "#" + dataSnapshot.child("club").child(user_club).child("just_color").getValue(String.class);
                    S.yame_count = dataSnapshot.child("yame_count").getValue(int.class);
                    mydang_name.setText(dataSnapshot.child("club").child(user_club).child("just_name").getValue(String.class));
                    mydang_people.setText(dataSnapshot.child("club").child(user_club).child("partisian").getValue(int.class).toString());
                    mydang_ideology.setText(dataSnapshot.child("club").child(user_club).child("just_idea").getValue(String.class));
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mRecycler = (RecyclerView) rootView.findViewById(R.id.my_jungdang_board);
        mRecycler.setHasFixedSize(true);

        TextView search=(TextView)rootView.findViewById(R.id.search_jungdang);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tran = manager.beginTransaction();
                Fragment frag = new MyJungdangFragment_SearchJungdang();
                tran.replace(R.id.my_jungdang_layout, frag);
                tran.addToBackStack(null);
                tran.commit();
            }
        });

        //return rootView;
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

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        mAdapter = new FirebaseRecyclerAdapter<JungPost, JungPostViewHolder>(JungPost.class, R.layout.myjungdang_item_post,
                JungPostViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final JungPostViewHolder viewHolder, final JungPost model, final int position) {
                final DatabaseReference postRef = getRef(position);
                // Set click listener for the whole post view
                final String postKey = postRef.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), JungPostDetailActivity.class);
                        intent.putExtra(JungPostDetailActivity.EXTRA_POST_KEY, postKey);
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
                        DatabaseReference globalPostRef = mDatabase.child("jung_posts/").child(postRef.getKey());
                        Log.d("log1",globalPostRef.toString());
                        DatabaseReference userPostRef = mDatabase.child("jung_user-posts").child(model.uid).child(postRef.getKey());
                        Log.d("log2",userPostRef.toString());
                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });
            }


        };
        mRecycler.setAdapter(mAdapter);

    }

    // [START post_stars_transaction]
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                JungPost p = mutableData.getValue(JungPost.class);

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

    public Query getQuery(DatabaseReference databaseReference) {
        // [START recent_posts_query]
        // Last 100 posts, these are automatically the 100 most recent
        // due to sorting by push() keys
        Query recentPostsQuery = databaseReference.child("jung_posts/"+S.club_id)
                .limitToFirst(100);
        // [END recent_posts_query]

        return recentPostsQuery;
    }


}
