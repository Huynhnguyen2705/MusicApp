package com.learnandroid.huynh.music_app

import Entity.Track
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.taishi.library.Indicator

/**
 * Created by Huynh on 12/9/2017.
 */
class TrackAdapter(context: Context, resource: Int, list: ArrayList<Track>) :
        ArrayAdapter<Track>(context, resource, list) {

    val list = ArrayAdapter<Track>(context, resource, list)
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

        var view: View = p1!!
        if (view == null) {
            view = LayoutInflater.from(p2!!.context).inflate(R.layout.image_custom_view, null, false)

        }

        var track: Track = getItem(p0) as Track

        // get view
        val imageView = view.findViewById<ImageView>(R.id.imageTrack) as ImageView
        val nameView = view.findViewById<TextView>(R.id.nameTrack) as TextView
        val inca = view.findViewById<com.taishi.library.Indicator>(R.id.indicator) as Indicator

        //set image
        Glide.with(imageView.context)
                .load(track.imageURL)
                .into(imageView)

        //set text
        nameView.text = track.name

        inca.visibility = View.GONE

        return view

    }

}