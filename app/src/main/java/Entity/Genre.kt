package Entity

import android.util.Log
import com.google.firebase.database.*

/**
 * Created by Huynh on 12/5/2017.
 */
class Genre(var id: String, var name: String) {

    /**
     * Push genre info to firebase realtime databas
     * @param v_object: genre will be pushed to Firebase DB
     * @return: a genre_id pushed
     */

    fun pushInfoToFDB(v_object: Genre): String {
        // Firebase Database
        val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseReference = mFirebaseDatabase.reference
        var ID: String = mDatabaseReference.push().key
        v_object.id = ID
        mDatabaseReference.child("genres")
        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.e("Genres upload", "Upload error " + p0.toString())
            }

            override fun onDataChange(data: DataSnapshot?) {
                if (!data!!.child(v_object.name).exists()) {
                    mDatabaseReference.child("genres").child(v_object.name).setValue(v_object)
                }
            }

        })

        return ID
    }

    // map fields of genre to push and insert tracks field
    @Exclude
    fun toMap(trackinfo: HashMap<String, Boolean>): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("id", id)
        result.put("name", name)
        result.put("tracks", trackinfo)

        return result
    }

    /**
     * Push artist update
     */
    fun pushUpdateTrackInfo(track: Track, genre: Genre) {
        // Firebase Database
        val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseReference = mFirebaseDatabase.reference
        mDatabaseReference.push().key
        mDatabaseReference.child("genres")
        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.e("Genre upload", "Update error" + p0.toString())
            }
            override fun onDataChange(p0: DataSnapshot?) {
                if (!p0!!.child(genre.name).child("tracks").child(track.name).exists()) {

//                    // value to push update
//                    val trackinfo = HashMap<String, Boolean>()
//                    trackinfo.put(track_ID, true)
//                    //value exist in album node and value to push update
//                    val album_values = genre.toMap(trackinfo)
//
//                    // update the node
///                    val childUpdate = HashMap<String, Any>()
///                   childUpdate.put("/genres/" + genre.name+"/tracks", trackinfo)
//                    mDatabaseReference.child("genres").child(genre.name)
//                    mDatabaseReference

                    //childUpdate.put("/tracks/" + track_name + "/" + genre.id, album_values)


                    //update to firebase
//                    mDatabaseReference.child("genres").child(genre.name).child("tracks").updateChildren(trackinfo as Map<String, Any>?)
                    mDatabaseReference.child("genres").child(genre.name).child("tracks").
                            child(track.name).setValue(track)
                }
            }

        })
    }

}