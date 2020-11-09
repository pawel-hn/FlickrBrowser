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
        Log.d("PHN", "doingbackground ${params[0]}")

        return URL(params[0]).readText()
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        Log.d("PHN DownloadFlickrJSon", "result ok")
        listener.jsonDownloaded(result!!)
    }
}