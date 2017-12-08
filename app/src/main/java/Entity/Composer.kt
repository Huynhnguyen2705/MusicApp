package Entity

import android.util.Log
import com.google.firebase.database.*


class Composer(var id: String, val name: String, val imageURL: String) {

    /**
     * Push composer info to firebase realtime databas
     * @param v_object:composer will be pushed to Firebase DB
     * @return: a composer_id pushed
     */
    fun pushInfoToFDB(v_object: Composer): String {
        // Firebase Database
        val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseReference = mFirebaseDatabase.reference
        val ID: String = mDatabaseReference.push().key
        v_object.id = ID

        mDatabaseReference.child("composers")
        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (!p0!!.child(v_object.name).exists()) {
                    mDatabaseReference.child("composers").child(v_object.name).setValue(v_object)
                }
            }

        })

        return ID
    }

    // map fields of composer to push and insert tracks field
    @Exclude
    fun toMap(trackinfo: HashMap<String, Boolean>): Map<String, Any> {
        val result = HashMap<String, Any>()
        result.put("id", id)
        result.put("name", name)
        result.put("imageURL", imageURL)
        result.put("tracks", trackinfo)

        return result
    }

    /**
     * Push composer update
     */
    fun pushUpdateTrackInfo(track_ID: String, track_name: String, composer: Composer) {
        // Firebase Database
        val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseReference = mFirebaseDatabase.reference
        mDatabaseReference.child("composers")
        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                Log.e("Composer upload", "Update error" + p0.toString())
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (!p0!!.child(composer.name).child("tracks").child(track_ID).exists()) {

                    // value to push update
                    val trackinfo = HashMap<String, Boolean>()
                    trackinfo.put(track_ID, true)
                    //value exist in composer node and value to push update
                    val album_values = composer.toMap(trackinfo)

                    // update the node
                    val childUpdate = HashMap<String, Any>()
                    childUpdate.put("/composers/" + composer.name, album_values)

                    // update to firebase
                    mDatabaseReference.updateChildren(childUpdate)

                }
            }

        })
    }
}