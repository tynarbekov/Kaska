package io.jachoteam.kaska.screens.postDetails

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.jachoteam.kaska.R
import io.jachoteam.kaska.models.Image

class FragmentPostDetailsVideo : AbstractFragmentPostDetails() {

    private var videoPlayer: MediaPlayer = MediaPlayer()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_post_details_video, container, false)

        return view
    }

    override fun releaseMedia() {
        videoPlayer.release()
        // do smth
    }

    override fun setMedia(activity: Activity, url: String) {
    }

    override fun setImagesMap(map: Map<String, Image>) {
        // do nothing
    }

}