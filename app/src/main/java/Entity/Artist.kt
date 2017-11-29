package Entity


class Artist {
    private var ID: Long = 0
    private var name: String = ""
    private var image_url: String = ""
    private var wiki_link: String = ""
    private var albums: Boolean = false
    private var listening_count: Int = 0 // sum of listening count value from track of artist
    private var tracks: Boolean = false
    private var users: Boolean = false
    private var favourite: Boolean = false
}
