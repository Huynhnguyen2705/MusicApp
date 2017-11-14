package com.learnandroid.huynh.music_app

import android.app.Activity
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
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity(), OnceFragment.OnFragmentInteractionListener,
        TwiceFragment.OnFragmentInteractionListener, ThriceFragment.OnFragmentInteractionListener {


    private var mAuth: FirebaseAuth? = null
    private var mAhthStateListener: FirebaseAuth.AuthStateListener? = null
    private var mUsername: String? = null
    private val RC_SIGN_IN = 1234

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var currentUser: FirebaseUser? = mAuth?.currentUser
        mAhthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            if (currentUser != null) {
                // user is signed-in
                Toast.makeText(this, "Welcome " + currentUser.displayName, Toast.LENGTH_SHORT).show()
            } else {
                // user is signed-out
                val providers = listOf<AuthUI.IdpConfig>(
                        AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                        AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
                )
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(), RC_SIGN_IN)
            }
        }

        // Fire base Auth
        mAuth = FirebaseAuth.getInstance()

        val toolbar = findViewById<Toolbar>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val listOfFragment: MutableList<Fragment> = mutableListOf<Fragment>()
        val onceFragment: Fragment = OnceFragment()
        val twiceFragment: Fragment = TwiceFragment()
        val thriceFragment: Fragment = ThriceFragment()
        listOfFragment.add(onceFragment)
        listOfFragment.add(twiceFragment)
        listOfFragment.add(thriceFragment)
        val viewPager = findViewById<ViewPager>(R.id.viewpager) as ViewPager
        setupViewPager(viewPager,listOfFragment)

        val tabLayout = findViewById<TabLayout>(R.id.tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)


    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accodingly

        mAhthStateListener?.let { mAuth?.addAuthStateListener(it) }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sign_out_menu -> {
                AuthUI.getInstance().signOut(this)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
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

    private fun setupViewPager(viewPager: ViewPager, listOfFragment: MutableList<Fragment>) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(listOfFragment[0], "ONE")
        adapter.addFragment(listOfFragment[1], "TWO")
        adapter.addFragment(listOfFragment[2], "THREE")
        viewPager.adapter = adapter
    }


    override fun onFragmentInteraction(uri: Uri) {
    }

}


internal class ViewPagerAdapter(manager: android.support.v4.app.FragmentManager) : FragmentPagerAdapter(manager) {
    private val mFragmentList: MutableList<android.support.v4.app.Fragment> = mutableListOf<android.support.v4.app.Fragment>()
    private val mFragmentTitleList: MutableList<String> = mutableListOf<String>()
    private val mActivityList: MutableList<Activity> = mutableListOf<Activity>()

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
