package pawel.hn.flickrbrowser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_flickr.*


class MainActivity : AppCompatActivity(),
    GetFlickrJson.JsonDownloaded,
    ParseFlickrJson.JsonParsed {

    private val flickrAdapter = FlickrRecyclerViewAdapter(this, ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

    }

    private fun createUri(baseURL: String, searchCriteria: String, matchAll: Boolean): String {
        return Uri.parse(baseURL).buildUpon().appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1").build().toString()
    }

    override fun jsonDownloaded(data: String) {
        ParseFlickrJson(this).execute(data)

    }

    override fun jsonParsed(data: ArrayList<FlickrPhoto>) {
        Log.d("PHN FickrActivty", "PHN items")
        flickrAdapter.loadNewData(data)
        rvFlickr.layoutManager = LinearLayoutManager(this)
        //rvFlickr.addOnItemTouchListener(RecyclerItemClickListener(this, rvFlickr, this))

        rvFlickr.adapter = flickrAdapter
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.flickr_search_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.app_bar_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity
            
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            isIconifiedByDefault = true // Do not iconify the widget; expand it by default
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
                tvFlickrSearchResult.text = query
                val url = createUri(
                    "https://www.flickr.com/services/feeds/photos_public.gne",
                    query, true
                )
                GetFlickrJson(this)
                    .execute(url)
            }
        }
    }
}