package pawel.hn.flickrbrowser

import android.app.Dialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        Toast.makeText(this, "hello", Toast.LENGTH_LONG).show()

        GetFlickrJson(this)
            .execute("http://www.flickr.com/services/feeds/photos_public.gne?tags=trees&format=json&nojsoncallback=1")

    }

    private fun createUri(baseURL: String, searchCriteria: String, matchAll: Boolean): String {
        return Uri.parse(baseURL).buildUpon().appendQueryParameter("tags", searchCriteria)
            .appendQueryParameter("tagmode", if (matchAll) "ALL" else "ANY")
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1").build().toString()
    }

    override fun jsonDownloaded(data: String) {
        ParseFlickrJsonGSON().execute(data)

    }

    override fun jsonParsed(data: ArrayList<FlickrPhoto>) {
//        Log.d("PHN FickrActivty", "PHN items")
//        flickrAdapter.loadNewData(data)
//        rvFlickr.layoutManager = LinearLayoutManager(this)
//        //rvFlickr.addOnItemTouchListener(RecyclerItemClickListener(this, rvFlickr, this))
//
//        rvFlickr.adapter = flickrAdapter
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.flickr_search_menu, menu)

//        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        (menu.findItem(R.id.app_bar_search).actionView as SearchView).apply {
//            setSearchableInfo(searchManager.getSearchableInfo(componentName))
//            isIconifiedByDefault = true
//        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_about -> {
                Log.d("PHN", "menu about clicked")
                showAboutDialog()
            }
        }
        return true
    }

    private fun showAboutDialog() {


        AlertDialog.Builder(this)
            .setView(R.layout.dialog_about).setCancelable(true).show()
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_about)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }


    override fun onResume() {
        super.onResume()
//        if (Intent.ACTION_SEARCH == intent.action) {
//            intent.getStringExtra(SearchManager.QUERY)?.also { query ->
//                tvFlickrSearchResult.text = query
//                val url = createUri(
//                    "https://www.flickr.com/services/feeds/photos_public.gne",
//                    query, true
//                )
//                GetFlickrJson(this)
//                    .execute(url)
//            }
//        }


    }
}