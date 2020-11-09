package pawel.hn.flickrbrowser
import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlickrPhoto(
    val title: String = "",
    val author: String = "",
    val link: String = "",
    val image: String = "",
    val tags: String = ""
) : Parcelable