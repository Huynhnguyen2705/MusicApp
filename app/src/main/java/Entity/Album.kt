package Entity

import android.util.Log
import com.google.firebase.database.*


class Album(var id: String, var name: String, var imageURL: String, var year_release: String, var listening_count: Int) {

    /**
     * Push album info to firebase realtime databas
     * @param v_object:album will be pushed to Firebase DB
     * @return: an album_id pushed
     */
    fun pushInfoToFDB(v_object: Album): String {
        // Firebase Database
        val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseReference = mFirebaseDatabase.reference
        val ID: String = mDatabaseReference.push().key
        v_object.id = ID
        mDatabaseReference.child("albums")
        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (!p0!!.child(v_object.name).exists()) {
                    mDatabaseReference.child("albums").child(v_object.name).setValue(v_object)
                }
            }

        })




        return ID
    }

    // map fields of album to push and insert tracks field
    @Exclude
    fun toMap(trackinfo: HashMap<String, Boolean>): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("id", id)
        result.put("name", name)
        result.put("imageURL", imageURL)
        result.put("year_release", year_release)
        result.put("listening_count", listening_count)
        result.put("tracks", trackinfo)

        return result
    }


    /**
     * Push album update
     */
    fun pushUpdateTrackInfo(track_ID: String, track_name: String, album: Album) {
        // Firebase Database
        val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseReference = mFirebaseDatabase.reference
        mDatabaseReference.child("albums").child(album.id).child("tracks").child(track_ID)
        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.e("Album upload", "Update error" + p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (!p0!!.exists()) {

                    // value to push update
                    val trackinfo = HashMap<String, Boolean>()
                    trackinfo.put(track_ID, true)
                    //value exist in album node and value to push update
                    val album_values = album.toMap(trackinfo)

                    // update the node
                    val childUpdate = HashMap<String, Any>()
                    childUpdate.put("/albums/" + album.id, album_values)

                    // update to firebase
                    mDatabaseReference.updateChildren(childUpdate)

                }
            }

        })

    }
}