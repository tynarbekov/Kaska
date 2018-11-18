package io.jachoteam.kaska.screens.postDetails

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.jachoteam.kaska.ProfileViewActivity
import io.jachoteam.kaska.R
import io.jachoteam.kaska.common.SingleLiveEvent
import io.jachoteam.kaska.screens.comments.CommentsActivity
import io.jachoteam.kaska.screens.common.BaseActivity
import io.jachoteam.kaska.screens.common.setupAuthGuard
import io.jachoteam.kaska.screens.common.setupBottomNavigation
import io.jachoteam.kaska.screens.home.HomeActivity

import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : BaseActivity(), PostDetailsViewModel.Listener {

    private val TAG = "PostDetailActivity"

    private lateinit var postId: String
    private lateinit var userId: String
    private lateinit var mViewModel: PostDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        setSupportActionBar(toolbar)
        postId = intent.getStringExtra("postId")
        userId = intent.getStringExtra("userId")

        Log.d("test->PostDetailAct", "this.userId: $userId")
        Log.d("test->PostDetailAct", "this.postId: $postId")
        log(postId)

        setupAuthGuard { uid ->
            setupBottomNavigation(uid, 0)
            mViewModel = initViewModel()
            mViewModel.init(userId, postId, this)
            mViewModel.feedPost.observe(this, Observer {
                it?.let {
                    mViewModel.updatePost(it)
                    loadLikes()
                }
            })
            mViewModel.goToCommentsScreen.observe(this, Observer {
                it?.let { postId ->
                    CommentsActivity.start(this, postId)
                }
            })
        }
    }

    override fun loadLikes() {
//        if (mViewModel.getLikes() == null) {
            mViewModel.loadLikes(postId).observe(this, Observer {
                it?.let { feedPostLikes -> mViewModel.updatePostLikes(feedPostLikes) }
            })
//        }
    }

    override fun openComments() {
        mViewModel.openComments(postId)
    }

    override fun toggleLike() {
        mViewModel.toggleLike(postId)
    }

    override fun openProfile(username: String, uid: String) {
        Log.d(HomeActivity.TAG, "VIEW PROFILE: $username, $uid")
        val profileIntent = Intent(this, ProfileViewActivity::class.java)
        profileIntent.putExtra("uid", uid);
        profileIntent.putExtra("username", username);
        startActivity(profileIntent)
    }

    override fun getActivity() = this


    fun log(text: String) {
        Log.d(TAG, "postId: $text")
    }

}
