package io.jachoteam.kaska.screens.postDetails

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import io.jachoteam.kaska.R
import io.jachoteam.kaska.models.Image
import io.jachoteam.kaska.screens.common.GlideApp

class PostDetailsSlidingImageAdapter(private var context: Context,
                                     private var images: List<Image>): PagerAdapter() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)
    private lateinit var imageView: ImageView

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun getCount() = images.size

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }


    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout: View = inflater
                .inflate(R.layout.feed_sliding_image, view, false)!!
        imageView = imageLayout.findViewById(R.id.feed_sliding_image)

        updateViews(position)

        view.addView(imageLayout, 0)
        return imageLayout
    }

    private fun updateViews(position: Int) {
        GlideApp.with(context)
                .load(images[position].url)
                .centerCrop()
                .apply (RequestOptions.bitmapTransform(RoundedCorners(80)))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .into(imageView)
    }

}