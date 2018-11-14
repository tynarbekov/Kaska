package io.jachoteam.kaska.screens.home

import android.content.Context
import android.support.v4.view.PagerAdapter
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

class FeedSlidingImageAdapter(private var context: Context, private var images: List<Image>) : PagerAdapter() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    private lateinit var imageView: ImageView
    private lateinit var imageViewPrev: ImageView
    private lateinit var imageViewNext: ImageView

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
            TODO("change current image by clicking on previews")
        }
        imageViewNext.setOnClickListener {
            TODO("change current image by clicking on previews")
        }

        view.addView(imageLayout, 0)
        return imageLayout
    }

    private fun updateViews(position: Int) {
        GlideApp.with(context)
                .load(images[position].url)
                .centerCrop()
                .apply (RequestOptions.bitmapTransform(RoundedCorners(100)))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false)
                .into(imageView)
        if (position > 0) {
            GlideApp.with(context)
                    .load(images[position - 1].url)
                    .centerCrop()
                    .apply (RequestOptions.bitmapTransform(RoundedCorners(20)))
                    .override(120,120)
                    .placeholder(R.drawable.image_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .into(imageViewPrev)
        }
        if (position < images.size - 1) {
            GlideApp.with(context)
                    .load(images[position + 1].url)
                    .centerCrop()
                    .apply (RequestOptions.bitmapTransform(RoundedCorners(30)))
                    .override(120,120)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .into(imageViewNext)
        }
    }
}
