package io.jachoteam.kaska.screens.postDetails

import android.app.Activity
import android.content.Context
import android.content.Intent

interface PostDetailsService {

    var activity: Activity

    fun openPostDetails(userId:String, postId: String)
}