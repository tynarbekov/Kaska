package io.jachoteam.kaska.screens.postDetails

import android.app.Activity
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import io.jachoteam.kaska.R
import io.jachoteam.kaska.models.Image

class FragmentPostDetailsVoice : AbstractFragmentPostDetails() {

    private var audioPlayer: MediaPlayer = MediaPlayer()

    private lateinit var playBtn: Button
    private lateinit var pauseBtn: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_post_details_voice, container, false)
        prepareAudioPlayer(view)
        return view
    }

    private fun prepareAudioPlayer(view: View) {

        playBtn = view.findViewById(R.id.btn_play_audio)
        pauseBtn = view.findViewById(R.id.btn_pause_audio)

        playBtn.setOnClickListener {
            audioPlayer.start()
        }

        pauseBtn.setOnClickListener {
            if (audioPlayer.isPlaying) {
                audioPlayer.pause()
            }
        }


    }

    override fun setMedia(activity: Activity, url: String) {
        val uri: Uri = Uri.parse(url)
        try {
            audioPlayer = MediaPlayer().apply {
                setAudioStreamType(AudioManager.STREAM_MUSIC)
                setDataSource(activity, uri)
                prepare()
                isLooping = true
            }
        } catch (exception: Exception) {
            return
        }

    }

    override fun releaseMedia() {
        audioPlayer.release()
    }

    override fun setImagesMap(map: Map<String, Image>) {
        // do nothing
    }

}