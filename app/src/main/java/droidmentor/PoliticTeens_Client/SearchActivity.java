package droidmentor.PoliticTeens_Client;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.greenfrvr.hashtagview.HashtagView;

import java.util.Arrays;
import java.util.List;

import droidmentor.PoliticTeens_Client.models.Jundang;
import droidmentor.PoliticTeens_Client.viewholder.ManyJungPostViewHolder;

/**
 * Created by User on 2017-09-23.
 */

public class SearchActivity extends Activity {
    HashtagView hashtagView2;
    public static final List<String> DATA = Arrays.asList("android", "library", "collection",
            "hashtags", "min14sdk", "UI", "view", "github", "opensource", "project", "widget");
    public List<String> DATA2;
    public int i=0;

    private FirebaseRecyclerAdapter<Jundang, ManyJungPostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    DatabaseReference mDatabase;
    int partisian;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        hashtagView2 = (HashtagView) findViewById(R.id.hashtags2);
        hashtagView2.setData(DATA, new HashtagView.DataTransform<String>() {
            @Override
            public CharSequence prepare(String item) {
                SpannableString spannableString = new SpannableString("#" + item);
                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#26303D")), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                return spannableString;
            }
        });

        hashtagView2.addOnTagSelectListener(new HashtagView.TagsSelectListener() {
            @Override
            public void onItemSelected(Object item, boolean selected) {
                Log.d("tqtqtq",hashtagView2.getSelectedItems().toString());
            }
        });


        mRecycler = (RecyclerView) findViewById(R.id.search_dang_list);
        mRecycler.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    @Override
    protected void onStart() {
        super.onStart();

        mManager = new LinearLayoutManager(getApplicationContext());
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);


        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        mAdapter = new FirebaseRecyclerAdapter<Jundang, ManyJungPostViewHolder>(Jundang.class, R.layout.dang_item,
                ManyJungPostViewHolder.class, postsQuery) {
            @Override
            protected void populateViewHolder(final ManyJungPostViewHolder viewHolder, final Jundang model, final int position) {
                final DatabaseReference postRef = getRef(position);
                // Set click listener for the whole post view
                final String postKey = postRef.getKey();

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //alert띄워주기 가입창
                        final AlertDialog.Builder alert = new AlertDialog.Builder(SearchActivity.this);
                        alert.setTitle("알림");
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (S.club_id != -1){
                                    Toast.makeText(getApplicationContext(), "이미 가입된 정당이 있습니다!",Toast.LENGTH_SHORT).show();
                                    alert.show().dismiss();
                                }
                                else {

                                    mDatabase.child("users").child(GetUserId.getUid()).child("club").setValue(Integer.parseInt(postRef.getKey()));
                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            partisian = dataSnapshot.child("club").child(postRef.getKey()).child("partisian").getValue(int.class);
                                            Log.d("user",partisian+"");
                                            mDatabase.child("club").child(postRef.getKey()).child("partisian").setValue((partisian+1));
                                            Toast.makeText(getApplicationContext(), "이미 가입된dsa다!"+partisian,Toast.LENGTH_SHORT).show();
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });

                                }
                            }

                        });
                        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "취소 버튼이 눌렸습니다",Toast.LENGTH_SHORT).show();
                            }
                        });
                        alert.setMessage("이 정당에 가입하시겠습니까?");
                        alert.show();
                    }
                });
                // Determine if the current user has liked this post and set UI accordingly

                // Bind Post to ViewHolder, setting OnClickListener for the star button
                viewHolder.bindToPost(model);
            }


        };
        mRecycler.setAdapter(mAdapter);

    }


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
        Query recentPostsQuery = databaseReference.child("club/");
        // [END recent_posts_query]

        return recentPostsQuery;
    }

}
