package pawel.hn.flickrbrowser

import android.util.Log
import java.net.URL

class GetFlickrJson(private val listener: JsonDownloaded): CoroutinesAsyncTask<String, Void, String>() {


    interface JsonDownloaded {
        fun jsonDownloaded(data: String)
    }


    override fun doInBackground(vararg params: String?): String {
        return URL(params[0]).readText()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        Log.d("PHN DownloadFlickrJSon", "result PHN")
        listener.jsonDownloaded(result!!)
    }
}