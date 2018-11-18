package io.jachoteam.kaska.screens.postDetails

import android.app.Activity
import android.content.Context
import android.content.Intent

class DefaultPostDetailsImpl(override var activity: Activity) : PostDetailsService {
    override fun openPostDetails(userId: String,postId: String) {
        val postDetailsIntent = Intent(activity as Context, PostDetailActivity::class.java)
        postDetailsIntent.putExtra("postId", postId)
        postDetailsIntent.putExtra("userId", userId)
        activity.startActivity(postDetailsIntent)
    }
}