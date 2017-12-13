package com.learnandroid.huynh.music_app

import Entity.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import wseemann.media.FFmpegMediaMetadataRetriever


class MainActivity : AppCompatActivity(), OnceFragment.OnFragmentInteractionListener,
        TwiceFragment.OnFragmentInteractionListener, ThriceFragment.OnFragmentInteractionListener {


    // Firebase Storage
    var mStorageRef: StorageReference? = null
    // Firebase Authentication variable
    var mAuth: FirebaseAuth? = null
    // Listener Auth
    var mAhthStateListener: AuthStateListener? = null
    // an arbitrary request code value
    val RC_SIGN_IN: Int = 1234

    // request code for upload track
    val RC_UPLOAD: Int = 12345

    // Fragments - the items on tab layout
    var listOfFragment: MutableList<Fragment> = mutableListOf()

    // FFmpegMetadataRetriever
    var mmr: FFmpegMediaMetadataRetriever? = null

    var user: Entity.User? = null
    var track: Track? = null
    var album: Album? = null
    var artist: Artist? = null
    var composer: Composer? = null
    var genre: Genre? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // TODO: check if device doesn't have INTERNET

        // list of fragments - list of tab items
        val onceFragment: Fragment = OnceFragment()
        val twiceFragment: Fragment = TwiceFragment()
        val thriceFragment: Fragment = ThriceFragment()
        // add them to list
        listOfFragment.add(onceFragment)
        listOfFragment.add(twiceFragment)
        listOfFragment.add(thriceFragment)


        // Fire base Auth
        mAuth = FirebaseAuth.getInstance()



        //Firebase storage reference
        mStorageRef = FirebaseStorage.getInstance().reference

        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val viewPager = findViewById<ViewPager>(R.id.viewpager) as ViewPager
        val tabLayout = findViewById<TabLayout>(R.id.tabs) as TabLayout
        setUpTabLayout(viewPager, tabLayout)

        login()
        user = pushInfoUser()
//        val uploadBtn = findViewById<Button>(R.id.upload) as Button
//
//        uploadBtn.setOnClickListener { it: View? ->
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "audio/*"
//            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
//            // create choser and call onActivityResult() with Request code = RC_UPLOAD
//            startActivityForResult(Intent.createChooser(intent, "Complete action"), RC_UPLOAD)
//        }


    }


    /**
     * This func to push info current user
     * @param user: user info will be pushed
     * @return: a user pushed
     */
    fun pushInfoUser(): User {
        // Firebase Database
        val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseReference = mFirebaseDatabase.reference

        val userID: String = mDatabaseReference.push().key
        val userName: String = mAuth?.currentUser?.displayName.toString()
        val userEmail: String = mAuth?.currentUser?.email.toString()

        var v_user = User(userID, userName, userEmail)

        mDatabaseReference.child("users").child(userID).child("email")
        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {

            // check duplicate users
            override fun onDataChange(data: DataSnapshot?) {
                if (!data?.exists()!!) {
                    mDatabaseReference.child("users").child(userID).setValue(v_user,
                            { databaseError: DatabaseError?, databaseReference: DatabaseReference ->
                        Log.v("userinfo", "Successful. Or error" + databaseError)

                    })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        })
        return v_user
    }


    /**
     * This func use Firebase AuthUI to create UI and log in from Email, Google and Facebook account
     */
    private fun login() {
        if (mAuth?.currentUser == null) {
            // Choose authentication providers
            val providers: List<AuthUI.IdpConfig> = listOf(
                    AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                    AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build())

// Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .build(),
                    RC_SIGN_IN)
        }

    }

    /**
     * This funs set up the Tab layout at this Activity
     * @param viewPager: the viewpager is set up by func: setupViewPager(viewPager:ViewPager, MutableList<Fragment>)
     * @param tabLayout: the tabLayout that you want to set up
     */
    private fun setUpTabLayout(viewPager: ViewPager, tabLayout: TabLayout) {
        // listOfFragment:
        setupViewPager(viewPager, listOfFragment)
        tabLayout.setupWithViewPager(viewPager)
    }

    /**
     * this func set up the viewPager
     * @param viewPager: the viewpager that you want to set up
     * @param listOfFragment:  the list of Fragments is showed in the set up tab layout
     */
    private fun setupViewPager(viewPager: ViewPager, listOfFragment: MutableList<Fragment>) {
        if (listOfFragment.size > 0) {
            val adapter = ViewPagerAdapter(supportFragmentManager) // using the custom viewpager adapter
            adapter.addFragment(listOfFragment[0], getString(R.string.fragment_one)) // add fragment to the adapter and set its title
            adapter.addFragment(listOfFragment[1], getString(R.string.fragment_two))
            adapter.addFragment(listOfFragment[2], getString(R.string.fragment_three))
            viewPager.adapter = adapter //set viewpager's adapter
        }
    }

    /**
     * this func use in login() func by Firebase AuthUI
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var userName: String = mAuth?.currentUser?.displayName.toString() // userName from current user info
        if (requestCode == RC_SIGN_IN) {// if needing sign in
            if (resultCode == Activity.RESULT_OK) {// sign in successfully
                Toast.makeText(this, "Welcome " + userName, Toast.LENGTH_SHORT).show()//welcome user
            } else if (requestCode == Activity.RESULT_CANCELED) { //if user cancel the sign in
                finish()
            }
        } else if (requestCode == RC_UPLOAD && resultCode == Activity.RESULT_OK) {
            val selectedTrackUri: Uri? = data?.data // uri from a chosen audio

            mmr = FFmpegMediaMetadataRetriever()
            val path = getRealPathFromURI_API19(this, selectedTrackUri!!)
            mmr!!.setDataSource(path)
            val trackTitle = mmr!!.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE)
            // Get a reference to store file at  userName/tracks/<FILENAME>
            val trackRef = mStorageRef?.child(userName)?.child("tracks")?.child(trackTitle)?.child(trackTitle + "_track")
            // Upload audio file to Firebase Storage
            trackRef?.putFile(selectedTrackUri)?.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                run {
                    val downloadUrl: Uri? = taskSnapshot?.downloadUrl
                    // Set the download URL to the message box, so that the user can send it to the database
                    val trackImageRef = mStorageRef?.child(userName)?.child("tracks")?.child(trackTitle)?.child(trackTitle + "_image")
                    // Upload Track Image file to Firebase Storage
                    trackImageRef?.putBytes(mmr!!.embeddedPicture)?.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                        run {
                            val downloadTrackImageUrl: Uri? = taskSnapshot?.downloadUrl

                            val album_name = mmr!!.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM)
                            val album_date_release = mmr!!.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DATE)
                            val artist_name = mmr!!.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST)
                            val composer_name = mmr!!.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_COMPOSER) ?: ""
                            val genre_name = mmr!!.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_GENRE)
                            val genre_track = HashMap<String, Boolean>()
                            album = Album("", album_name, downloadTrackImageUrl.toString(), album_date_release, 0)
                            artist = Artist("", artist_name, downloadTrackImageUrl.toString(), 0)

                            // check if the audio file has composer
                            var composer_id: String = ""
                            if (composer_name != "") {
                                composer = Composer("", composer_name, downloadTrackImageUrl.toString())
                                composer!!.pushInfoToFDB(composer!!)
                            }
                            genre = Genre("", genre_name)

                            album!!.pushInfoToFDB(album!!)
                            artist!!.pushInfoToFDB(artist!!)

                            genre!!.pushInfoToFDB(genre!!)
                            track = Track("", trackTitle, downloadTrackImageUrl.toString(), downloadUrl.toString(),
                                    album_name, artist_name, composer_name, genre_name, 0)
                            val track_id = track!!.pushInfoToFDB(track!!)
                            album!!.pushUpdateTrackInfo(track_id, trackTitle, album!!)
                            artist!!.pushUpdateTrackInfo(track_id, trackTitle, artist!!)
                            genre!!.pushUpdateTrackInfo(track!!, genre!!)
                            if (composer_name != "") composer!!.pushUpdateTrackInfo(track_id, trackTitle, composer!!)



//                        Log.v("trackInfo","Title: " + trackTitle)
//                        Log.v("trackInfo","ImageUrl: " + downloadTrackImageUrl.toString())
//                        Log.v("trackInfo","Link: " + downloadUrl.toString())

                            Toast.makeText(this, "SUCCESSFULLY!", Toast.LENGTH_SHORT).show()
                            mmr!!.release()

                        }
                    }?.addOnFailureListener { exception: java.lang.Exception -> Log.e("Error", "Error" + exception) }


                }
            }?.addOnFailureListener { exception: java.lang.Exception -> Log.e("Error", "Error" + exception) }

        }
    }

    fun getRealPathFromURI_API19(context: Context, uri: Uri): String {
        var filePath = ""
        val wholeID = DocumentsContract.getDocumentId(uri)

        // Split at colon, use second item in the array
        val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]

        val column = arrayOf(MediaStore.Audio.Media.DATA)

        // where id is equal to
        val sel = MediaStore.Audio.Media._ID + "=?"

        val cursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null)

        val columnIndex = cursor!!.getColumnIndex(column[0])

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex)
        }
        cursor.close()
        return filePath
    }



    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accodingly

        mAhthStateListener?.let { mAuth?.addAuthStateListener(it) }

    }

    override fun onResume() {
        super.onResume()
        mAhthStateListener?.let { mAuth?.addAuthStateListener(it) }

    }

    // create menu view
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }


    /**
     * the sign out func use AuthUI
     */
    private fun logOut() {
        AuthUI.getInstance()
                .signOut(this) // signing out when sign_out item clicked
                .addOnCompleteListener {
                    // user is now signed out
                    Toast.makeText(this@MainActivity, "You signed out", Toast.LENGTH_SHORT).show()
                    recreate() // reload this Activity to sign in another account
                }
    }

    /**
     * handle sign_out_menu_item clicked
     */

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out -> {
                logOut()
                true
            }
            R.id.upload -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "audio/*"
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                // create choser and call onActivityResult() with Request code = RC_UPLOAD
                startActivityForResult(Intent.createChooser(intent, "Complete Action"), RC_UPLOAD)
                true
            }
            R.id.setting -> {
                val settingintent = Intent(this, settingActivity::class.java)
                startActivity(settingintent)
                true
            }
            else -> super.onOptionsItemSelected(item)

        }
    }


    override fun onPause() {
        super.onPause()
        mAhthStateListener?.let { mAuth?.removeAuthStateListener(it) }
    }

    /* we don't have call beginTransaction() and commit everytime add or replace fragment
    * we can use multiple operations inside inTransaction block
    */
    inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().func().commit()
    }
    // add Fragment in one line: addFragment(fragment, R.id.fragment_container)
    /**
     * @param fragment: the fragment will add
     * @param frameId: the id of the View contains the @param fragment
     */
    fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    }

    // replace Fragment in one line: replaceFragment(fragment, R.id.fragment_container)
    /**
     * @param fragment: the fragment will replace
     * @param frameId: the id of the View contains the @param fragment
     */
    fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
        supportFragmentManager.inTransaction { replace(frameId, fragment) }
    }


    override fun onFragmentInteraction(uri: Uri) {
    }


}


internal class ViewPagerAdapter(manager: android.support.v4.app.FragmentManager) : FragmentPagerAdapter(manager) {
    private val mFragmentList = mutableListOf<Fragment>()
    private val mFragmentTitleList = mutableListOf<String>()

    override fun getItem(position: Int): android.support.v4.app.Fragment? {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: android.support.v4.app.Fragment, title: String) {
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mFragmentTitleList.get(position)
    }
}
