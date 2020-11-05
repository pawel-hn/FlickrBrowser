package pawel.hn.flickrbrowser

import org.json.JSONObject


class ParseFlickrJson(private val listener: JsonParsed): CoroutinesAsyncTask<String, Void, ArrayList<FlickrPhoto>>() {

    interface JsonParsed {
        fun jsonParsed(data: ArrayList<FlickrPhoto>)
    }

    override fun doInBackground(vararg params: String?): ArrayList<FlickrPhoto> {
        val jsonObject = JSONObject(params[0]!!)
        val jsonArray = jsonObject.getJSONArray("items")
        val itemsList = ArrayList<FlickrPhoto>()

        for (i in 0 until jsonArray.length()) {
            val jsonPhoto = jsonArray.getJSONObject(i)
            val title = jsonPhoto.getString("title")
            val author = jsonPhoto.getString("author")
            val tags = jsonPhoto.getString("tags")

            val jsonMedia = jsonPhoto.getJSONObject("media")
            val link = jsonMedia.getString("m").replaceFirst("_m.jpg", "_b.jpg")
            val photoUrl = jsonPhoto.getString("link")

            val imageItem = FlickrPhoto(title, author, link, photoUrl, tags)
            itemsList.add(imageItem)
        }
        return itemsList
    }

    override fun onPostExecute(result: ArrayList<FlickrPhoto>?) {
        super.onPostExecute(result)
        listener.jsonParsed(result!!)

    }
}