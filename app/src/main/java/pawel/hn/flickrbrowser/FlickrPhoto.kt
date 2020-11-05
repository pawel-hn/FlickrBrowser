package pawel.hn.flickrbrowser
import android.os.Parcel
import android.os.Parcelable


data class FlickrPhoto(
    val title: String = "",
    val author: String = "",
    val link: String = "",
    val image: String = "",
    val tags: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun toString(): String {
        return """FeedItemFlickrImage:
                (title='$title', 
                author='$author', 
                link='$link',
                image='$image',
                tags='$tags')
                *************************
                """.trimMargin()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeString(link)
        parcel.writeString(image)
        parcel.writeString(tags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FlickrPhoto> {
        override fun createFromParcel(parcel: Parcel): FlickrPhoto {
            return FlickrPhoto(parcel)
        }

        override fun newArray(size: Int): Array<FlickrPhoto?> {
            return arrayOfNulls(size)
        }
    }
}