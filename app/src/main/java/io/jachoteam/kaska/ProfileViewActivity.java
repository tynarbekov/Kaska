package io.jachoteam.kaska;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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

import io.jachoteam.kaska.dummy.DummyContent;
import io.jachoteam.kaska.models.User;
import io.jachoteam.kaska.screens.common.GlideApp;

public class ProfileViewActivity extends AppCompatActivity implements TabFragment.OnListFragmentInteractionListener,
        Tab2Fragment.OnFragmentInteractionListener, Tab3Fragment.OnFragmentInteractionListener{
    public String uid;
    public String username;
    String TAG = "ProfileViewActivity";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef;
    DatabaseReference postRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        username = intent.getStringExtra("username");
        Log.d(TAG, uid + ": " + username);

        userRef = database.getReference("users/" + uid);
        postRef = database.getReference("images/" + uid);
        countPosts();
        updateUserDetails();

        sendMessage();

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void sendMessage() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send_message);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(ProfileViewActivity.this, ChatActivity.class);
                startActivity(chatIntent);
            }
        });
    }

    private void updateUserDetails() {
        userRef.addValueEventListener(new ValueEventListener() {
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
    }

    private void countPosts() {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                TextView postsCount = (TextView) findViewById(R.id.posts_count_text);
                postsCount.setText(count + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        postRef.addListenerForSingleValueEvent(valueEventListener);
    }

    public void updateView(User user) {
        ImageView imageView = (ImageView) findViewById(R.id.profile_image);
        TextView username = (TextView) findViewById(R.id.username_text);
        TextView followersCount = (TextView) findViewById(R.id.followers_count_text);
        TextView followingCount = (TextView) findViewById(R.id.following_count_text);

        followersCount.setText(user.getFollowers().size() + "");
        followingCount.setText(user.getFollows().size() + "");
        username.setText(user.getUsername());

        GlideApp.with(this).load(user.getPhoto()).fallback(R.drawable.person).into(imageView);
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Log.i("INTERFACE_CALLEDD", "HREHEHE");
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.i("INTERFACE_CALLEDD", "URI URI URI");
    }
}
