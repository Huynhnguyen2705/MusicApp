package Entity

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Track(var id: String, var name: String, var imageURL: String, var trackURL: String,
            var album_id: String, var artist_id: String, var composer_id: String, var genre_id: String, var listeningCount: Int) {
//     var name: String =""
//
//     var imageURL: String = ""
//     var trackURL:String = ""
//     var listeningCount: Int = 0
//     var favoutire: Boolean = false
//     var artists: List<Boolean>? = null
//     var albums: List<Boolean>? = null
//     var composers: List<Boolean>? = null
//     var playLists: List<Boolean>? = null
//     var genres: List<Boolean>? = null
//     var users: List<Boolean>? = null

    /**
     * Push track info to firebase realtime databas
     * @param v_object:track will be pushed to Firebase DB
     * @return: a track_id pushed
     */
    fun pushInfoToFDB(v_object: Track): String {
        // Firebase Database
        val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        var mDatabaseReference = mFirebaseDatabase.reference
        var ID: String = mDatabaseReference.push().key
        v_object.id = ID
        mDatabaseReference.child("tracks")
        mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if (!p0!!.child(v_object.name).exists()) {
                    mDatabaseReference.child("tracks").child(v_object.name).setValue(v_object)
                }
            }

        })

        return ID
    }

}