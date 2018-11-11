package io.jachoteam.kaska.screens.home

import android.content.Context
import android.support.v4.view.ViewPager
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.jachoteam.kaska.R
import io.jachoteam.kaska.models.FeedPost
import io.jachoteam.kaska.models.Image
import io.jachoteam.kaska.screens.common.*
import kotlinx.android.synthetic.main.feed_item.view.*

class FeedAdapter(private val listener: Listener,
                  private var context: Context)
    : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    interface Listener {
        fun toggleLike(postId: String)
        fun loadLikes(postId: String, position: Int)
        fun openComments(postId: String)
        fun openProfile(username: String, uid: String)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    private var posts = listOf<FeedPost>()
    private var postLikes: Map<Int, FeedPostLikes> = emptyMap()
    private val defaultPostLikes = FeedPostLikes(0, false)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.feed_item, parent, false)
        return ViewHolder(view)
    }

    fun updatePostLikes(position: Int, likes: FeedPostLikes) {
        postLikes += (position to likes)
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        val likes = postLikes[position] ?: defaultPostLikes
        with(holder.view) {
            user_photo_image.loadUserPhoto(post.photo)
            username_text.text = post.username
            init(post.images,feed_slider_pager)
            if (likes.likesCount == 0) {
                likes_text.visibility = View.GONE
            } else {
                likes_text.visibility = View.VISIBLE
                val likesCountText = holder.view.context.resources.getQuantityString(
                        R.plurals.likes_count, likes.likesCount, likes.likesCount)
                likes_text.text = likesCountText
            }
            caption_text.setCaptionText(post.username, post.caption)
            like_image.setOnClickListener { listener.toggleLike(post.id) }
            username_text.setOnClickListener { listener.openProfile(post.username, post.uid) }
            like_image.setImageResource(
                    if (likes.likedByUser) R.drawable.ic_likes_active
                    else R.drawable.ic_likes_border)
            comment_image.setOnClickListener { listener.openComments(post.id) }
            listener.loadLikes(post.id, position)
        }
    }

    override fun getItemCount() = posts.size

    fun updatePosts(newPosts: List<FeedPost>) {
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(this.posts, newPosts) { it.id })
        this.posts = newPosts
        diffResult.dispatchUpdatesTo(this)
    }

    private fun init(imagesMap: Map <String, Image>,sliderPager: ViewPager){
        var imagesList: MutableList<Image> = mutableListOf()
        imagesMap.forEach { (key, value) ->
            run {
                imagesList.add(value)
            }
        }
        imagesList.sortBy { i -> i.order }
        HomeActivity.mPager = sliderPager
        HomeActivity.mPager.adapter = FeedSlidingImageAdapter(context, imagesList)
    }

}