package io.jachoteam.kaska.screens.postDetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.jachoteam.kaska.R

class FragmentPostDetailsVoice: AbstractFragmentPostDetails(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_post_details_voice, container,false)

        return view
    }
}