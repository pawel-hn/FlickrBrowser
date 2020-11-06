package pawel.hn.flickrbrowser

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.net.URL

class GetFlickrJson(private val listener: JsonDownloaded): CoroutinesAsyncTask<String, Void, String>() {


    interface JsonDownloaded {
        fun jsonDownloaded(data: String)
    }


    override fun doInBackground(vararg params: String?): String {

        var result = ""

        try {
            result = URL(params[0]).readText()
        } catch (e: Exception) {

            e.printStackTrace()
            Log.d("PNH GetFlickrJson", "error: ${e.message}")
        }

        return result
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        Log.d("PHN DownloadFlickrJSon", "result PHN")
        listener.jsonDownloaded(result!!)
    }
}