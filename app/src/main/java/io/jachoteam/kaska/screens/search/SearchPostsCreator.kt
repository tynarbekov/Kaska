package io.jachoteam.kaska.screens.search

import android.arch.lifecycle.Observer
import android.util.Log
import io.jachoteam.kaska.common.BaseEventListener
import io.jachoteam.kaska.common.Event
import io.jachoteam.kaska.common.EventBus
import io.jachoteam.kaska.data.SearchRepository
import io.jachoteam.kaska.models.SearchPost

class SearchPostsCreator(searchRepo: SearchRepository) : BaseEventListener() {
    init {
        EventBus.events.observe(this, Observer {
            it?.let { event ->
                when (event) {
                    is Event.CreateFeedPost -> {
                        val searchPost = with(event.post) {
                            SearchPost(
                                    image = image,
                                    caption = caption,
                                    postId = id)
                        }
                        searchRepo.createPost(searchPost).addOnFailureListener {
                            Log.d(TAG, "Failed to create search post for event: $event", it)
                        }
                    }
                    else -> {
                    }
                }
            }
        })
    }

    companion object {
        const val TAG = "SearchPostsCreator"
    }
}