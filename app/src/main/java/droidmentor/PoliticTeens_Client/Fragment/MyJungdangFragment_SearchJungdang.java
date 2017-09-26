package droidmentor.PoliticTeens_Client.Fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.greenfrvr.hashtagview.HashtagView;

import java.util.Arrays;
import java.util.List;

import droidmentor.PoliticTeens_Client.GetUserId;
import droidmentor.PoliticTeens_Client.JungPostDetailActivity;
import droidmentor.PoliticTeens_Client.MainActivity;
import droidmentor.PoliticTeens_Client.R;
import droidmentor.PoliticTeens_Client.S;
import droidmentor.PoliticTeens_Client.SearchActivity;
import droidmentor.PoliticTeens_Client.models.Jundang;
import droidmentor.PoliticTeens_Client.models.JungPost;
import droidmentor.PoliticTeens_Client.viewholder.JungPostViewHolder;
import droidmentor.PoliticTeens_Client.viewholder.ManyJungPostViewHolder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyJungdangFragment_SearchJungdang extends Fragment {

    FragmentManager manager;  //Fragment를 관리하는 클래스의 참조변수
    FragmentTransaction tran;  //실제로 Fragment를 추가/삭제/재배치 하는 클래스의 참조변수
    ImageButton go_search;
    private FirebaseRecyclerAdapter<Jundang, ManyJungPostViewHolder> mAdapter;
    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;
    DatabaseReference mDatabase;
    int partisian;

    public MyJungdangFragment_SearchJungdang() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myjungdangs_searchjungdang, container, false);
        manager = (FragmentManager) getFragmentManager();

        mRecycler = (RecyclerView) view.findViewById(R.id.hot_jungdang);
        mRecycler.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        TextView my = (TextView) view.findViewById(R.id.my_jungdang);
        my.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.beginTransaction().remove(MyJungdangFragment_SearchJungdang.this).commit();
                manager.popBackStack();

            }
        });

        go_search = (ImageButton)view.findViewById(R.id.go_search);

        go_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
            }
        });
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
                        final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                        alert.setTitle("알림");
                        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (S.club_id != -1){
                                    Toast.makeText(getActivity(), "이미 가입된 정당이 있습니다!",Toast.LENGTH_SHORT).show();
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
                                            Toast.makeText(getActivity(), "이미 가입된dsa다!"+partisian,Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), "취소 버튼이 눌렸습니다",Toast.LENGTH_SHORT).show();
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
        Query recentPostsQuery = databaseReference.child("club/")
                .limitToFirst(5);
        // [END recent_posts_query]

        return recentPostsQuery;
    }




}
