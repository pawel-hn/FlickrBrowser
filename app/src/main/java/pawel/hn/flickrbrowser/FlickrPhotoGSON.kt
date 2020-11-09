package pawel.hn.flickrbrowser

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class FlickrPhotoGSON(
    var title: String = "",
    var link: String = "",
    var description: String = "",
    var modified: String = "",
    var generator: String = "",
    var items: List<PhotoArrayGSON>,

    ) : Parcelable

@Parcelize
data class PhotoArrayGSON(
    var title: String = "",
    var link: String = "",
    var media: MediaGSON,
    var date_taken: String = "",
    var description: String = "",
    var published: String = "",
    var author: String = "",
    var author_id: String = "",
    var tags: String = ""
) : Parcelable

@Parcelize
data class MediaGSON(
    var m: String = ""
) : Parcelable