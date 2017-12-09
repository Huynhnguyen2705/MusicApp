package com.learnandroid.huynh.music_app

import Entity.Track
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_online.*
import kotlinx.android.synthetic.main.image_custom_view.view.*

class OnlineActivity : AppCompatActivity() {

    // childListener to read data from firebase
    var childEventListener: ChildEventListener? = null

    //Listview
    var listView: ListView? = null
    lateinit var mTrackAdapter: TrackAdapter

    //firebase reference
    val mDatabaseFirebase = FirebaseDatabase.getInstance()
    var mDatabaseRef = mDatabaseFirebase.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online)

        // get extra string from Home Screen, value: Hottest Songs
        val image = intent.extras.getString("Image")
        // display image depend on string extras from intent
        getStringExtras(image)

        // Initial listview and track adapter
        var trackList = ArrayList<Track>()
        mTrackAdapter = TrackAdapter(this, R.layout.item_listview_cus, trackList)
        listView?.adapter = mTrackAdapter

    }

    fun getStringExtras(image: String) {
        if (image == "hottest") {
            val image: Int = R.drawable.hostest_song
            imageHeader.imageSongsBanner.setBackgroundResource(image)
            attachDatabaseReadListener()
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

        childEventListener = object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String) {
                val track = dataSnapshot.value as Track
                mTrackAdapter.add(track)

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        mDatabaseRef.addChildEventListener(childEventListener)
    }

    private fun detachDatabaseReadListener() {
        if (childEventListener != null) {
            mDatabaseRef.removeEventListener(childEventListener)
            childEventListener = null
        }
    }

    override fun onPause() {
        super.onPause()
        detachDatabaseReadListener()
    }
}
