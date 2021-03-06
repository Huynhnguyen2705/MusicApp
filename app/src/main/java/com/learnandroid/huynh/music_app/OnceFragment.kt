package com.learnandroid.huynh.music_app

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_fragment_one.*
import kotlinx.android.synthetic.main.image_custom_view.view.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [OnceFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [OnceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnceFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    val hottestImage: Int = R.drawable.hostest_song
    val electronicImage: Int = R.drawable.electronic
    val urbanImage: Int = R.drawable.urban
    val countryImage: Int = R.drawable.country
    val rockImage: Int = R.drawable.rock
    val latinImage: Int = R.drawable.latin



    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_fragment_one, container, false)
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // hottest banner
        hottestBanner.imageSongsBanner.setBackgroundResource(hottestImage)
        electronicBanner.imageSongsBanner.setBackgroundResource(electronicImage)
        urbanBanner.imageSongsBanner.setBackgroundResource(urbanImage)
        countryBanner.imageSongsBanner.setBackgroundResource(countryImage)
        rockBanner.imageSongsBanner.setBackgroundResource(rockImage)
        latinBanner.imageSongsBanner.setBackgroundResource(latinImage)
    }
    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onStart() {
        super.onStart()
        try {
            mListener = activity as OnFragmentInteractionListener
        } catch (e: ClassCastException) {
            throw ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener")
        }

    }

    override fun onResume() {
        super.onResume()

        hottestBanner.imageSongsBanner.setOnClickListener {
            val onlineIntent = Intent(this.context, OnlineActivity::class.java)
            onlineIntent.putExtra("Image", "hottest")
            startActivity(onlineIntent)
        }
        electronicBanner.imageSongsBanner.setOnClickListener {
            val onlineIntent = Intent(this.context, OnlineActivity::class.java)
            onlineIntent.putExtra("Image", "electronic")
            startActivity(onlineIntent)
        }
        urbanBanner.imageSongsBanner.setOnClickListener {
            val onlineIntent = Intent(this.context, OnlineActivity::class.java)
            onlineIntent.putExtra("Image", "urban")
            startActivity(onlineIntent)
        }
        countryBanner.imageSongsBanner.setOnClickListener {
            val onlineIntent = Intent(this.context, OnlineActivity::class.java)
            onlineIntent.putExtra("Image", "country")
            startActivity(onlineIntent)
        }
        rockBanner.imageSongsBanner.setOnClickListener {
            val onlineIntent = Intent(this.context, OnlineActivity::class.java)
            onlineIntent.putExtra("Image", "rock")
            startActivity(onlineIntent)
        }
        latinBanner.imageSongsBanner.setOnClickListener {
            val onlineIntent = Intent(this.context, OnlineActivity::class.java)
            onlineIntent.putExtra("Image", "latin")
            startActivity(onlineIntent)
        }


    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OnceFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): OnceFragment {
            val fragment = OnceFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
