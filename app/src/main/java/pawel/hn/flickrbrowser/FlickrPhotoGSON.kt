package pawel.hn.flickrbrowser


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlickrPhotoGSON(
    var title: String = "",
    var link: String = "",
    var description: String = "",
    var items: List<PhotoArrayGSON>,

    ) : Parcelable

@Parcelize
data class PhotoArrayGSON(
    var title: String = "",
    var link: String = "",
    var media: MediaGSON,
    var description: String = "",
    var author: String = "",
    var author_id: String = "",
    var tags: String = ""
) : Parcelable

@Parcelize
data class MediaGSON(
    var m: String = ""
) : Parcelable {
    fun replaceMwithB(): String {
        return m.replaceFirst("_m.jpg", "_b.jpg")
    }
}