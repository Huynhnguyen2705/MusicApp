package com.learnandroid.huynh.music_app

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_online.*
import kotlinx.android.synthetic.main.image_custom_view.view.*

class OnlineActivity : AppCompatActivity() {

    // childListener to read data from firebase
    var childEventListener: ChildEventListener? = null

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

        childEventListener = object : ChildEventListener {

            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String) {


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
