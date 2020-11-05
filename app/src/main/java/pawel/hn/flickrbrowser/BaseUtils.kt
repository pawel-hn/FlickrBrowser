package pawel.hn.flickrbrowser

import android.app.Activity
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


internal const val FLICKR_QUERY = "FLICKR_QUERY"
internal const val FLICKR_TRANSFER = "FLICKR_TRANSFER"
const val STORAGE_PERMISSION_CODE = 1

open class BaseUtils(): AppCompatActivity() {

    private var listener: BackPressed? = null

    fun activateToolbar(enableHome: Boolean) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(enableHome)


        toolbar.setNavigationOnClickListener {
            Log.d("PHN base utils", "navigation button clicked")
            if (listener != null) {
                listener!!.backPressed()
            }
        }
    }

    interface BackPressed {
        fun backPressed()
    }

    fun setOnClickListener(listener: BackPressed) {
        this.listener  = listener
    }

}