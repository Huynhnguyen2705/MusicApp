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
        val image = intent.extras.getString("Image")

        if (image == "hottest") {
            val image: Int = R.drawable.hostest_song
            imageHeader.imageSongsBanner.setBackgroundResource(image)
        } else if (image == "electronic") {
            val image: Int = R.drawable.electronic
            imageHeader.imageSongsBanner.setBackgroundResource(image)
        } else if (image == "urban") {
            val image: Int = R.drawable.urban
            imageHeader.imageSongsBanner.setBackgroundResource(image)
        } else if (image == "country") {
            val image: Int = R.drawable.country
            imageHeader.imageSongsBanner.setBackgroundResource(image)
        } else if (image == "rock") {
            val image: Int = R.drawable.rock
            imageHeader.imageSongsBanner.setBackgroundResource(image)
        } else if (image == "latin") {
            val image: Int = R.drawable.latin
            imageHeader.imageSongsBanner.setBackgroundResource(image)
        }
    }
}
