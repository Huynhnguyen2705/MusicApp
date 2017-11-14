package com.learnandroid.huynh.music_app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_online.*
import kotlinx.android.synthetic.main.image_custom_view.view.*

class OnlineActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online)

        // get extra string from Home Screen, value: Hottest Songs
        val hottestString = intent.getStringExtra("typeOfSongs")
        imageHeader.textSongsBanner.text = hottestString
    }
}
