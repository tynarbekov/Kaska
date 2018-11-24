package io.jachoteam.kaska.screens.postDetails

import android.app.Activity
import android.support.v4.app.Fragment
import io.jachoteam.kaska.models.Image

abstract class AbstractFragmentPostDetails : Fragment() {
    abstract fun setMedia(activity: Activity, url: String)

    abstract fun releaseMedia()
    //    fun values()
    abstract fun setImagesMap(map: Map<String, Image>)
}