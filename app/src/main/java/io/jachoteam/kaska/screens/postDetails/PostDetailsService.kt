package io.jachoteam.kaska.screens.postDetails

import android.app.Activity
import android.content.Intent

interface PostDetailsService {

    var activity: Activity

    fun openPostDetails(postId: String) {
         val postDetailsIntent = Intent(activity,PostDetailActivity::class.java)
         postDetailsIntent.putExtra("uid", postId)
         activity.startActivity(postDetailsIntent)
    }
}