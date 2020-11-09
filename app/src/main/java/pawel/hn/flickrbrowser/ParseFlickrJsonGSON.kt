package pawel.hn.flickrbrowser

import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import org.json.JSONObject


class ParseFlickrJsonGSON() : CoroutinesAsyncTask<String, Void, ArrayList<PhotoArrayGSON>>() {

//    interface JsonParsedGSON {
//        fun jsonParsedGSON(data: ArrayList<PhotoArrayGSON>)
//    }

    override fun doInBackground(vararg params: String?): ArrayList<PhotoArrayGSON> {

        val itemsList = ArrayList<PhotoArrayGSON>()

        try {

            val responseData = Gson().fromJson(params[0], FlickrPhotoGSON::class.java)

            for (item in responseData.items.indices) {
                itemsList.add(responseData.items[item])
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("PNH ParsJson", "error: ${e.message}")
        }
        return itemsList


    }

    override fun onPostExecute(result: ArrayList<PhotoArrayGSON>?) {
        super.onPostExecute(result)
        if (result != null) {
            Log.d("PHN", "first photo ${result[0]}")
        } else {
            Log.d("PHN", "nic nie sparsowalo")
        }


        //if(result != null) listener.jsonParsedGSON(result)


    }
}