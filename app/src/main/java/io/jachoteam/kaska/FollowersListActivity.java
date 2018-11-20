package io.jachoteam.kaska;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.jachoteam.kaska.adapter.FollowersAdapter;
import io.jachoteam.kaska.models.User;

public class FollowersListActivity extends AppCompatActivity {
    public String TAG = "FollowersListActivity";
    public String uid = "";
    public User user;
    public ImageView goBack;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference followersRef;
    DatabaseReference singleFollowerRef;
    List<String> followersUid = new ArrayList<>();
    List<User> followers = new ArrayList<>();

    private ListView listView;
    private FollowersAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_list);
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        Log.i(TAG, uid);

        goBack = (ImageView) findViewById(R.id.back_image);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FINISH", "FOLLOWERS LIST ACTIVITY");
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.list_followers);
        final ArrayList<User> moviesList = new ArrayList<>();
        mAdapter = new FollowersAdapter(this, moviesList);
        listView.setAdapter(mAdapter);

        followersRef = database.getReference("users/" + uid + "/followers");

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
                            moviesList.add(dataSnapshot.getValue(User.class));
                            mAdapter = new FollowersAdapter(getApplicationContext(), moviesList);
                            listView.setAdapter(mAdapter);
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
