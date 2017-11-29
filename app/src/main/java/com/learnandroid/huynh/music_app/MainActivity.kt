package com.learnandroid.huynh.music_app

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentTransaction
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity(), OnceFragment.OnFragmentInteractionListener,
        TwiceFragment.OnFragmentInteractionListener, ThriceFragment.OnFragmentInteractionListener {


    // Firebase Authentication variable
    private var mAuth: FirebaseAuth? = null
    // Listener Auth
    private var mAhthStateListener: AuthStateListener? = null
    // Current User is logging
    private var currentUser: FirebaseUser? = null
    // an arbitrary request code value
    private val RC_SIGN_IN: Int = 1234
    // Fragments - the items on tab layout
    private var listOfFragment: MutableList<Fragment> = mutableListOf()

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

        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewPager = findViewById<ViewPager>(R.id.viewpager) as ViewPager
        val tabLayout = findViewById<TabLayout>(R.id.tabs) as TabLayout
        setUpTabLayout(viewPager, tabLayout)

        login()

    }

    /**
     * This func use Firebase AuthUI to create UI and log in from Email, Google and Facebook account
     */
    private fun login() {
        if (currentUser == null) {
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
            adapter.addFragment(listOfFragment[0], "ONE") // add fragment to the adapter and set its title
            adapter.addFragment(listOfFragment[1], "TWO")
            adapter.addFragment(listOfFragment[2], "THREE")
            viewPager.adapter = adapter //set viewpager's adapter
        }
    }

    /**
     * this func use in login() func by Firebase AuthUI
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        currentUser = mAuth?.currentUser // login successfully and having current user info
        var userName: String = currentUser?.displayName.toString() // userName from current user info
        if (requestCode == RC_SIGN_IN) {// if needing sign in
            if (resultCode == Activity.RESULT_OK) {// sign in successfully
                Toast.makeText(this, "Welcome " + userName, Toast.LENGTH_SHORT).show()//welcome user
            } else if (requestCode == Activity.RESULT_CANCELED) { //if user cancel the sign in
                finish()
            }
        }
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
