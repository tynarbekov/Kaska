package io.jachoteam.kaska;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.jachoteam.kaska.models.User;
import io.jachoteam.kaska.screens.common.GlideApp;

public class ProfileViewActivity extends AppCompatActivity {
    String TAG = "ProfileViewActivity";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String uid = intent.getStringExtra("uid");
        String username = intent.getStringExtra("username");
        Log.d(TAG, uid + ": " + username);

        ref = database.getReference("users/" + uid);
        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                updateView(user);
                System.out.println(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void updateView(User user) {
        ImageView imageView = (ImageView) findViewById(R.id.profile_image);
        TextView postsCount = (TextView) findViewById(R.id.posts_count_text);
        TextView username = (TextView) findViewById(R.id.username_text);
        TextView followersCount = (TextView) findViewById(R.id.followers_count_text);
        TextView followingCount = (TextView) findViewById(R.id.following_count_text);
        followersCount.setText(user.getFollowers().size() + "");
        followingCount.setText(user.getFollows().size() + "");
        username.setText(user.getUsername());

        GlideApp.with(this).load(user.getPhoto()).fallback(R.drawable.person).into(imageView);

        // TODO load images for posts count
//        postsCount.setText(user.);
    }

}
