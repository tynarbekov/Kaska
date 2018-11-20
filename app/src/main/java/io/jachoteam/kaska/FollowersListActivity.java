package io.jachoteam.kaska;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.jachoteam.kaska.models.User;

public class FollowersListActivity extends AppCompatActivity {
    public String TAG = "FollowersListActivity";
    public String uid = "";
    public User user;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference followersRef;
    DatabaseReference singleFollowerRef;
    List<String> followersUid = new ArrayList<>();
    List<User> followers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_list);
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        Log.i(TAG, uid);

        followersRef = database.getReference("users/" + uid + "/follows");

        followersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("FollowersLoaded", dataSnapshot.toString());
                for (DataSnapshot imageSnapshot : dataSnapshot.getChildren()) {
                    String followerUid = imageSnapshot.getKey();
                    followersUid.add(followerUid);
                    Log.i("Followers", followerUid);
                    singleFollowerRef = database.getReference("users/" + followerUid);
                    singleFollowerRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            followers.add(dataSnapshot.getValue(User.class));
                            Log.i("FOLLOWER_USER", dataSnapshot.toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }
}
