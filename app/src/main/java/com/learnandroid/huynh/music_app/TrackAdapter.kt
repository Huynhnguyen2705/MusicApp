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

/**
 * Created by Huynh on 12/9/2017.
 */
class TrackAdapter(context: Context, var resource: Int, var trackList: ArrayList<Track>) :
        ArrayAdapter<Track>(context, resource, trackList) {


    var vi: LayoutInflater

    init {
        this.resource = resource
        this.vi = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view: View? = convertView

        if (convertView == null) {
            view = vi.inflate(resource, null)

        }

        var track = trackList[position]

        // get view
        val imageView = view!!.findViewById<ImageView>(R.id.imageTrack)
        val nameView = view.findViewById<TextView>(R.id.nameTrack)
        val artistView = view.findViewById<TextView>(R.id.artistTrack)
        val inca = view.findViewById<com.taishi.library.Indicator>(R.id.indicator)

        //set image
        Glide.with(imageView!!.context)
                .load(track.imageURL)
                .into(imageView)

        //set text
        nameView?.text = track.name
        artistView?.text = track.artist_name

        inca?.visibility = View.GONE

        return view

    }
}