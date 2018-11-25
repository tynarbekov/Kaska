package io.jachoteam.kaska.screens.postDetails

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.util.Log
import io.jachoteam.kaska.ProfileViewActivity
import io.jachoteam.kaska.R
import io.jachoteam.kaska.screens.comments.CommentsActivity
import io.jachoteam.kaska.screens.common.BaseActivity
import io.jachoteam.kaska.screens.common.setupAuthGuard
import io.jachoteam.kaska.screens.common.setupBottomNavigation
import io.jachoteam.kaska.screens.home.HomeActivity

import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.View
import io.jachoteam.kaska.models.FeedPost


class PostDetailActivity : BaseActivity(), PostDetailsViewModel.Listener {

    private val TAG = "PostDetailActivity"

    private lateinit var postId: String
    private lateinit var userId: String
    private lateinit var mViewModel: PostDetailsViewModel

    private lateinit var postDetailsSectionPageAdapter: PostDetailsSectionPageAdapter
    private lateinit var postDetailsViewPager: ViewPager
    private val fragmentsMap: MutableMap<String, AbstractFragmentPostDetails> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        postId = intent.getStringExtra("postId")
        userId = intent.getStringExtra("userId")

        postDetailsSectionPageAdapter = PostDetailsSectionPageAdapter(supportFragmentManager)

        postDetailsViewPager = findViewById(R.id.post_details_section_page_container)

        val tabLayout = findViewById<TabLayout>(R.id.section_tabs)
        tabLayout.setupWithViewPager(postDetailsViewPager)

        setupAuthGuard { uid ->
            setupBottomNavigation(uid, 0)
            mViewModel = initViewModel()
            mViewModel.init(userId, postId, this)
            mViewModel.feedPost.observe(this, Observer {
                it?.let {
                    mViewModel.updatePost(it)
//                    initFragmentsMap(it)
                    AsyncLoad(it).run()
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
        mViewModel.loadLikes(postId).observe(this, Observer {
            it?.let { feedPostLikes -> mViewModel.updatePostLikes(feedPostLikes) }
        })
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
        profileIntent.putExtra("uid", uid)
        profileIntent.putExtra("username", username)
        startActivity(profileIntent)
    }

    override fun getActivity() = this


    fun log(text: String) {
        Log.d(TAG, "postId: $text")
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = PostDetailsSectionPageAdapter(supportFragmentManager)
        fragmentsMap.forEach { title, fragment ->
            run {
                if (fragment != null) {
                    adapter.addFragment(fragment, title)
                }
            }
        }
        viewPager.adapter = adapter
    }

    private fun initFragmentsMap(post: FeedPost) {
        if (post.images.size > 0) {
            val fragmentSlidesPostDetails: AbstractFragmentPostDetails = FragmentSlidesPostDetails()
            fragmentSlidesPostDetails.setImagesMap(post.images)
            fragmentsMap["Images"] = fragmentSlidesPostDetails
        }

        if (post.audioUrl.length > 0) {
            val fragmentPostDetailsVoice: AbstractFragmentPostDetails = FragmentPostDetailsVoice()
            fragmentPostDetailsVoice.setMedia(this, post.audioUrl)
            fragmentsMap["Voice"] = fragmentPostDetailsVoice
        }
        // implement for video
        setupViewPager(postDetailsViewPager)
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentsMap.forEach { _, fragment ->
            run {
                fragment.releaseMedia()
            }
        }
    }

    inner class AsyncLoad(internal val post: FeedPost) : Runnable {
        override fun run() {
            initFragmentsMap(post)
        }
    }
}
