package io.jachoteam.kaska.screens.postDetails

import android.app.Activity
import android.arch.lifecycle.LiveData
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.tasks.OnFailureListener
import io.jachoteam.kaska.R
import io.jachoteam.kaska.common.SingleLiveEvent
import io.jachoteam.kaska.data.FeedPostsRepository
import io.jachoteam.kaska.data.common.map
import io.jachoteam.kaska.models.FeedPost
import io.jachoteam.kaska.screens.common.BaseViewModel
import io.jachoteam.kaska.screens.common.loadUserPhoto
import io.jachoteam.kaska.screens.home.FeedPostLikes

class PostDetailsViewModel(onFailureListener: OnFailureListener,
                           private val feedPostsRepo: FeedPostsRepository) : BaseViewModel(onFailureListener) {

    interface Listener {
        fun toggleLike()
        fun loadLikes()
        fun openComments()
        fun openProfile(username: String, uid: String)
        fun getActivity(): Activity
    }

    lateinit var postId: String
    lateinit var userId: String
    lateinit var feedPost: LiveData<FeedPost>
    lateinit var post: FeedPost
    private lateinit var livePostLike: LiveData<FeedPostLikes>
    private lateinit var postLikes: FeedPostLikes
    private val _goToCommentsScreen = SingleLiveEvent<String>()
    val goToCommentsScreen = _goToCommentsScreen

    private lateinit var listener: Listener

    private lateinit var userImageView: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var likeImageView: ImageView
    private lateinit var commentImageView: ImageView
    private lateinit var shareImageView: ImageView
    private lateinit var likeTextView: TextView
    private lateinit var captionTextView: TextView


    fun init(userId: String, postId: String, listener: Listener) {
//        Log.d("PostDetailsViewModel", "this.userId: ${this.userId}")
//        Log.d("PostDetailsViewModel", "this.postId: ${this.postId}")
        if (!this::postId.isInitialized && !this::userId.isInitialized) {
            this.userId = userId
            this.postId = postId
            Log.d("test->PostDetViewModel", "assigned.userId: $userId")
            Log.d("test->PostDetViewModel", "assigned.postId: $postId")
            feedPost = feedPostsRepo.getFeedPost(userId, postId)
        }

        this.listener = listener
        initViews()
    }

    fun getLikes() = livePostLike

    fun loadLikes(postId: String): LiveData<FeedPostLikes> {
        if (!this::livePostLike.isInitialized) {
            val liveData = feedPostsRepo.getLikes(postId).map { likes ->
                FeedPostLikes(
                        likesCount = likes.size,
                        likedByUser = likes.find { it.userId == userId } != null)
            }
            livePostLike = liveData
        }

        return livePostLike
    }


    fun updatePostLikes(postLikes: FeedPostLikes) {
        this.postLikes = postLikes

        updatePostLikes()
    }

    fun updatePost(post: FeedPost) {
        this.post = post
        updatePost()
    }

    private fun updatePost() {
        userImageView.loadUserPhoto(post.photo)
        userNameTextView.text = post.username

        captionTextView.text = post.caption
        likeImageView.setOnClickListener{ listener.toggleLike()}
        userNameTextView.setOnClickListener { listener.openProfile(post.username, post.uid) }
        userImageView.setOnClickListener { listener.openProfile(post.username, post.uid) }
        commentImageView.setOnClickListener{ listener.openComments()}
    }

    private fun updatePostLikes(){
        if (postLikes.likesCount == 0) {
            likeTextView.visibility = View.GONE
        } else {
            likeTextView.visibility = View.VISIBLE
            likeTextView.text = "likes: ${postLikes.likesCount}"
        }
        likeImageView.setImageResource(
                if ( postLikes.likedByUser) R.drawable.ic_likes_active
                else R.drawable.ic_likes_border)
    }

    fun toggleLike(postId: String) {
        feedPostsRepo.toggleLike(postId, userId).addOnFailureListener(onFailureListener)
    }

    fun openComments(postId: String) {
        _goToCommentsScreen.value = postId
    }

    private fun initViews() {
        userImageView = listener.getActivity().findViewById(R.id.post_user_photo_image)
        userNameTextView = listener.getActivity().findViewById(R.id.post_username_text)
        likeImageView = listener.getActivity().findViewById(R.id.post_like_image)
        commentImageView = listener.getActivity().findViewById(R.id.post_comment_image)
        shareImageView = listener.getActivity().findViewById(R.id.post_share_image)
        likeTextView = listener.getActivity().findViewById(R.id.post_likes_text)
        captionTextView = listener.getActivity().findViewById(R.id.post_caption_text)
    }
}