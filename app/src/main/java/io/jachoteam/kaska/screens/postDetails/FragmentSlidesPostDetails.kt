package io.jachoteam.kaska.screens.postDetails

import android.app.Activity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.jachoteam.kaska.R
import io.jachoteam.kaska.models.Image

class FragmentSlidesPostDetails: AbstractFragmentPostDetails() {

    private var imagesMap: Map<String, Image> = mutableMapOf()
    private lateinit var postDetailsSlidingImageAdapter: ViewPager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_post_details_slides, container, false)
        postDetailsSlidingImageAdapter = view.findViewById(R.id.post_details_slider_pager)
        postDetailsSlidingImageAdapter.adapter = PostDetailsSlidingImageAdapter(view.context, getImagesList())
        Log.d("test->","onCreateView()")
        return view
    }

    private fun getImagesList(): MutableList<Image> {
        val imagesList: MutableList<Image> = mutableListOf()
        imagesMap.forEach { (key, value) ->
            run {
                imagesList.add(value)
            }
        }
        return imagesList
    }

    override fun releaseMedia() {
        // do nothing
    }

    override fun setMedia(activity: Activity, url: String) {
    }

    override fun setImagesMap(map: Map<String,Image>) {
        imagesMap = map
    }
}