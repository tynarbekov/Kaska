package io.jachoteam.kaska.screens.home

import android.content.Context
import android.os.Parcelable
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import io.jachoteam.kaska.R
import io.jachoteam.kaska.models.Image
import io.jachoteam.kaska.screens.common.GlideApp

class FeedSlidingImageAdapter : PagerAdapter {
    private var images = listOf<Image>()

    private lateinit var inflater: LayoutInflater
    private lateinit var context: Context

    lateinit var imageView: ImageView
    lateinit var imageViewPrev: ImageView
    lateinit var imageViewNext: ImageView

    constructor(context: Context, images: List<Image>) {
        this.images = images
        this.context = context
        inflater = LayoutInflater.from(context)
    }

    constructor() : super()

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout: View = inflater
                .inflate(R.layout.feed_sliding_image, view, false)!!
        imageView = imageLayout.findViewById(R.id.feed_sliding_image)
        imageViewPrev = imageLayout.findViewById(R.id.feed_sliding_image_prev)
        imageViewNext = imageLayout.findViewById(R.id.feed_sliding_image_next)

        updateViews(position)

        imageViewPrev.setOnClickListener {
            //            updateViews(position - 1)
            instantiateItem(view, position - 1)
            Log.d("click", "PREV: " + position.toString())
        }
        imageViewNext.setOnClickListener {
//            updateViews(position + 1)
            instantiateItem(view, position + 1)
            Log.d("click", "NEXT: " + position.toString())
        }

        view.addView(imageLayout, 0)
        return imageLayout
    }

    private fun updateViews(position: Int) {
        Glide.with(context)
                .load(images[position].url)
                .into(imageView)
        if (position > 0) {
            GlideApp.with(context)
                    .load(images[position - 1].url)
                    .into(imageViewPrev)
        }
        if (position < images.size - 1) {
            GlideApp.with(context)
                    .load(images[position + 1].url)
                    .into(imageViewNext)
        }
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        super.restoreState(state, loader)
    }

    override fun saveState(): Parcelable? {
        return super.saveState()
    }
}
