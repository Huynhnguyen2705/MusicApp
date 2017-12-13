package com.learnandroid.huynh.music_app

import Entity.Track
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
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


        listView!!.onItemClickListener = AdapterView.OnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            val indicator = view1.findViewById<Indicator>(R.id.indicator)

//            if(playMusic(mTrackAdapter.getItem(i).trackURL)){
            indicator.visibility = View.VISIBLE
            val intent = Intent(this, MediaPlayerService::class.java)
            intent.action = MediaPlayerService.ACTION_PLAY
            startService(intent)

//            }else indicator.visibility = View.GONE
        }


    }

    fun playMusic(uriString: String): Boolean {
        var isPlaying = false
        val uri = Uri.parse(uriString)
        val player = MediaPlayer()
        player.setDataSource(this, uri)
        player.prepare()
        if (!player.isPlaying) {
            player.start()
            isPlaying = true
        } else if (player.isPlaying) {
            player.pause()
            isPlaying = false
        }
        return isPlaying
    }

    fun getStringExtras(image: String) {
        if (image == "hottest") {
            val image: Int = R.drawable.hostest_song
            imageHeader.imageSongsBanner.setBackgroundResource(image)
            attachDatabaseReadListener_Hottest()
        } else if (image == "electronic") {
            val image: Int = R.drawable.electronic
            imageHeader.imageSongsBanner.setBackgroundResource(image)
            attachDatabaseReadListener_Electric()
        } else if (image == "urban") {
            val image: Int = R.drawable.urban
            imageHeader.imageSongsBanner.setBackgroundResource(image)
            attackDatabaseReadListener_Urban()
        } else if (image == "country") {
            val image: Int = R.drawable.country
            imageHeader.imageSongsBanner.setBackgroundResource(image)
            attackDatabaseReadListener_Country()
        } else if (image == "rock") {
            val image: Int = R.drawable.rock
            imageHeader.imageSongsBanner.setBackgroundResource(image)
            attackDatabaseReadListener_Rock()
        } else if (image == "latin") {
            val image: Int = R.drawable.latin
            imageHeader.imageSongsBanner.setBackgroundResource(image)
            attackDatabaseReadListener_Latin()
        }
    }

    // load data at Hottest Songs
    fun attachDatabaseReadListener_Hottest() {
        mTrackAdapter.clear()
        mDatabaseRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                if (dataSnapshot.hasChildren()) {
                    val track = dataSnapshot.getValue(Track::class.java)
                    mTrackAdapter.add(track)
                }


            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {}
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    // load data at Electric songs
    fun attachDatabaseReadListener_Electric() {
        mTrackAdapter.clear()
        mDatabaseRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                if (dataSnapshot.hasChildren()) {
                    val track = dataSnapshot.getValue(Track::class.java)
                    if (track?.genre_name == "Electronic") {
                        mTrackAdapter.add(track)
                    }
                }

            }

            override fun onChildRemoved(p0: DataSnapshot?) {}

        })
    }

    // load data at at urban songs
    fun attackDatabaseReadListener_Urban() {
        mTrackAdapter.clear()
        mDatabaseRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                if (dataSnapshot.hasChildren()) {
                    val track = dataSnapshot.getValue(Track::class.java)
                    if (track?.genre_name == "Pop") {
                        mTrackAdapter.add(track)
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {}

        })
    }

    // load data at country songs
    fun attackDatabaseReadListener_Country() {
        mTrackAdapter.clear()
        mDatabaseRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                if (dataSnapshot.hasChildren()) {
                    val track = dataSnapshot.getValue(Track::class.java)
                    if (track?.genre_name == "Country") {
                        mTrackAdapter.add(track)
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {}

        })
    }

    // load data at rock songs
    fun attackDatabaseReadListener_Rock() {
        mTrackAdapter.clear()
        mDatabaseRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                if (dataSnapshot.hasChildren()) {
                    val track = dataSnapshot.getValue(Track::class.java)
                    if (track?.genre_name == "Rock") {
                        mTrackAdapter.add(track)
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {}

        })
    }

    //load data at latin songs
    fun attackDatabaseReadListener_Latin() {
        mTrackAdapter.clear()
        mDatabaseRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError?) {}
            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {}
            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {}
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                if (dataSnapshot.hasChildren()) {
                    val track = dataSnapshot.getValue(Track::class.java)
                    if (track?.genre_name == "Latin") {
                        mTrackAdapter.add(track)
                    }
                }
            }

            override fun onChildRemoved(p0: DataSnapshot?) {}

        })
    }

}
