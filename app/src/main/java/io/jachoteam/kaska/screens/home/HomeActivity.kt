package io.jachoteam.kaska.screens.home

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import io.jachoteam.kaska.ProfileViewActivity
import io.jachoteam.kaska.R
import io.jachoteam.kaska.screens.comments.CommentsActivity
import io.jachoteam.kaska.screens.common.BaseActivity
import io.jachoteam.kaska.screens.common.setupAuthGuard
import io.jachoteam.kaska.screens.common.setupBottomNavigation
import io.jachoteam.kaska.screens.profile.ProfileActivity
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : BaseActivity(), FeedAdapter.Listener {
    private lateinit var mAdapter: FeedAdapter
    private lateinit var mViewModel: HomeViewModel
    public var userUid = "";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Log.d(TAG, "onCreate")
        userUid = FirebaseAuth.getInstance().currentUser!!.uid

        mAdapter = FeedAdapter(this, this@HomeActivity)
        feed_recycler.adapter = mAdapter
        feed_recycler.layoutManager = LinearLayoutManager(this)

        setupAuthGuard { uid ->
            setupBottomNavigation(uid, 0)
            mViewModel = initViewModel()
            mViewModel.init(uid)
            mViewModel.feedPosts.observe(this, Observer {
                it?.let {
                    mAdapter.updatePosts(it)
                }
            })
            mViewModel.goToCommentsScreen.observe(this, Observer {
                it?.let { postId ->
                    CommentsActivity.start(this, postId)
                }
            })
        }
    }

    override fun toggleLike(postId: String) {
        Log.d(TAG, "toggleLike: $postId")
        mViewModel.toggleLike(postId)
    }

    override fun openProfile(username: String, uid: String) {
        Log.d(TAG, "VIEW PROFILE: $username, $uid")
        if (userUid!=null && uid.equals(userUid)) {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
            startActivity(intent)
            overridePendingTransition(0, 0)
        } else {
            val profileIntent = Intent(this, ProfileViewActivity::class.java)
            profileIntent.putExtra("uid", uid);
            profileIntent.putExtra("username", username);
            startActivity(profileIntent)
        }
    }

    override fun loadLikes(postId: String, position: Int) {
        if (mViewModel.getLikes(postId) == null) {
            mViewModel.loadLikes(postId).observe(this, Observer {
                it?.let { postLikes ->
                    mAdapter.updatePostLikes(position, postLikes)
                }
            })
        }
    }

    override fun openComments(postId: String) {
        mViewModel.openComments(postId)
    }

    companion object {
        const val TAG = "HomeActivity"

        // feed slider
        @SuppressLint("StaticFieldLeak")
        lateinit var mPager: ViewPager
    }
}