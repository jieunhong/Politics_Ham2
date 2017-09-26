package droidmentor.PoliticTeens_Client;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by User on 2017-09-14.
 */

public class SplashActivity extends Activity{
    boolean result;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.child("users").child(GetUserId.getUid()).child("club").getValue(int.class) >= 0) {
                        result = true;
                        S.club_id=dataSnapshot.child("users").child(GetUserId.getUid()).child("club").getValue(int.class);
                    }
                } catch (NullPointerException e) {
                    result = false;
                    S.club_id=-1;
                }
                Log.d("ㅅㅂ",""+S.club_id);
                Intent intent = new Intent(SplashActivity.this, FacebookLoginActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);



                Intent intent = new Intent(SplashActivity.this, FacebookLoginActivity.class);
                intent.putExtra("result",result);
                Log.d("ㅅㅂ2",""+result);
                startActivity(intent);
                finish();
            }
        };

        handler.sendEmptyMessageDelayed(0,5000);
        */
    }
}
