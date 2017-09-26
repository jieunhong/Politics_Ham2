package droidmentor.PoliticTeens_Client.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import droidmentor.PoliticTeens_Client.JungConPostDetailActivity;
import droidmentor.PoliticTeens_Client.JungPostDetailActivity;
import droidmentor.PoliticTeens_Client.R;
import droidmentor.PoliticTeens_Client.S;
import droidmentor.PoliticTeens_Client.models.JungConPost;
import droidmentor.PoliticTeens_Client.models.JungPost;
import droidmentor.PoliticTeens_Client.viewholder.JungConPostViewHolder;
import droidmentor.PoliticTeens_Client.viewholder.JungPostViewHolder;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class CongressFragment extends Fragment {


    TextView month_agenda;
    String month_agenda_string = "";
    private FirebaseRecyclerAdapter<JungConPost, JungConPostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    FragmentManager manager;  //Fragment를 관리하는 클래스의 참조변수
    private DatabaseReference mDatabase;

    public CongressFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("1","tq");
        View view = inflater.inflate(R.layout.fragment_congress, container, false);

        month_agenda = (TextView)view.findViewById(R.id.month_agenda_content);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        manager = (FragmentManager) getFragmentManager();
        // [START create_database_reference]
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                month_agenda_string = dataSnapshot.child("month_agenda").getValue(String.class);
                month_agenda.setText(month_agenda_string);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mRecycler = (RecyclerView) view.findViewById(R.id.month_agenda_list);
        mRecycler.setHasFixedSize(true);


        Log.d("tq",month_agenda_string+"gaga"+mDatabase.child("month_agenda"));


        return view;
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

        mAdapter = new FirebaseRecyclerAdapter<JungConPost, JungConPostViewHolder>(JungConPost.class, R.layout.congress_item_post,
                JungConPostViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final JungConPostViewHolder viewHolder, final JungConPost model, final int position) {
                final DatabaseReference postRef = getRef(position);
                // Set click listener for the whole post view
                final String postKey = postRef.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Launch PostDetailActivity
                        Intent intent = new Intent(getActivity(), JungConPostDetailActivity.class);
                        intent.putExtra(JungConPostDetailActivity.EXTRA_POST_KEY, postKey);
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
                        DatabaseReference globalPostRef = mDatabase.child("jung_all_posts/").child(postRef.getKey());
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
        Query recentPostsQuery = databaseReference.child("jung_all_posts")
                .limitToFirst(3);
        // [END recent_posts_query]

        return recentPostsQuery;
    }


}
