package com.learnandroid.huynh.music_app

import Entity.Track
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.taishi.library.Indicator
import kotlinx.android.synthetic.main.activity_online.*
import kotlinx.android.synthetic.main.image_custom_view.view.*

class OnlineActivity : AppCompatActivity() {



    //Listview
    var listView: ListView? = null
    lateinit var mTrackAdapter: TrackAdapter

    //firebase reference
    val mDatabaseFirebase = FirebaseDatabase.getInstance()
    var mDatabaseRef = mDatabaseFirebase.reference.child("tracks")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online)


        // get extra string from Home Screen, value: Hottest Songs
        val image = intent.extras.getString("Image")
        // Initial listview and track adapter
        listView = findViewById<ListView?>(R.id.listViewCustom)
        var trackList: ArrayList<Track> = ArrayList<Track>()
        mTrackAdapter = TrackAdapter(this, R.layout.item_listview_cus, trackList)
        listView?.adapter = mTrackAdapter

        // display image depend on string extras from intent
        getStringExtras(image)
        attachDatabaseReadListener()

        listView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val indicator = view1.findViewById<Indicator>(R.id.indicator)
            indicator.visibility = View.VISIBLE
        }


    }

    fun getStringExtras(image: String) {
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

    fun attachDatabaseReadListener() {

        mDatabaseRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                if (dataSnapshot.hasChildren()) {
                    val track = dataSnapshot.getValue(Track::class.java)
                    mTrackAdapter.add(track)
                }


            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }


}
