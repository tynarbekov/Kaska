package io.jachoteam.kaska.screens.postDetails

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import io.jachoteam.kaska.R

import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        setSupportActionBar(toolbar)


    }

}
