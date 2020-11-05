package pawel.hn.flickrbrowser

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_flickr_detail.*
import pawel.hn.flickrbrowser.R
import pawel.hn.flickrbrowser.BaseUtils
import pawel.hn.flickrbrowser.CoroutinesAsyncTask
import pawel.hn.flickrbrowser.FLICKR_TRANSFER
import pawel.hn.flickrbrowser.STORAGE_PERMISSION_CODE
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class FlickrDetailActivity : BaseUtils() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flickr_detail)
        activateToolbar(true)
        setOnClickListener(object: BackPressed{
            override fun backPressed() {
                onBackPressed()
            }
        })

        val photo = intent.getParcelableExtra<FlickrPhoto>(FLICKR_TRANSFER)

        if (photo != null) {
            tvFlickrDetailAuthor.text = photo.author
            tvFlickrDetailTitle.text = photo.title
            tvFlickrDetailTags.text = photo.tags

            Picasso.get()
                .load(photo.link)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(ivFlickrDetail)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.flickr_detail_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuFlickrSavePhoto -> {
                AlertDialog.Builder(this).setTitle("This will save current photo to internal storage")
                    .setPositiveButton("Yes") {dialog, _ ->
                        if (isReadStorageAllowed()) {
                            BitmapAsyncTask(getBitmapFromView(ivFlickrDetail)).execute()
                        } else {
                            //If the app don't have storage access permission we will ask for it.
                            requestStoragePermission()
                        }
                        dialog.dismiss()
                    }.setNegativeButton("Cancel") { dialog,_ ->
                        dialog.dismiss()
                    }.show()
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@FlickrDetailActivity,
                    "Permission granted, click again to save images.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this@FlickrDetailActivity,
                    "Oops you just denied the permission.",
                    Toast.LENGTH_LONG
                ).show()

            }
        }
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        val bitmap =
            Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun isReadStorageAllowed(): Boolean {

        val result = ContextCompat.checkSelfPermission(
            this, Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }
    private fun shouldRequestPermissionRationale(): Boolean {
        return Build.VERSION.SDK_INT >= 23 &&
                (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                        shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
    }

    private fun requestStoragePermission() {
        Log.d("PHN detail", "requestStoragePermission called")
        Log.d("PHN detail", "rationale: ${shouldRequestPermissionRationale()}")
        Log.d("PHN detail", "sdk: ${Build.VERSION.SDK_INT}")
        if (shouldRequestPermissionRationale()) {

            AlertDialog.Builder(this@FlickrDetailActivity).setMessage("Some rationale string")
                .setPositiveButton("GO TO SETTINGS") { _, _ ->
                    try {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity((intent))
                    } catch (e: ActivityNotFoundException) {
                        e.printStackTrace()
                    }
                }.setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.dismiss()
                }.show()
        }
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            STORAGE_PERMISSION_CODE
        )
    }

    private inner class BitmapAsyncTask(val bitmap: Bitmap?)
        :CoroutinesAsyncTask<Any, Void, String>(){

        override fun doInBackground(vararg params: Any?): String {
            var result = ""
            if (bitmap != null) {
                try {
                    val bytes = ByteArrayOutputStream()

                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)

                    val f = File(
                        externalCacheDir!!.absoluteFile.toString()
                                + File.separator + "FlickrPhoto" + System.currentTimeMillis() / 1000 + ".jpg"
                    )
                    val fo = FileOutputStream(f) // Creates a file output stream to write to the file represented by the specified object.
                    fo.write(bytes.toByteArray()) // Writes bytes from the specified byte array to this file output stream.
                    fo.close() // Closes this file output stream and releases any system resources associated with this stream. This file output stream may no longer be used for writing bytes.
                    result = f.absolutePath

                }catch (e: Exception) {
                    result = ""
                    e.printStackTrace()
                }
            }
            return  result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            if (result!!.isNotEmpty()) {
                Toast.makeText(
                    this@FlickrDetailActivity,
                    "File saved successfully to internal storage",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@FlickrDetailActivity,
                    "Something went wrong while saving the file.",
                    Toast.LENGTH_SHORT
                ).show()
            }

            MediaScannerConnection.scanFile(
                this@FlickrDetailActivity, arrayOf(result), null
            ) { path, uri ->
                // This is used for sharing the image after it has being stored in the storage.
                val shareIntent = Intent()
                shareIntent.action = Intent.ACTION_SEND
                shareIntent.putExtra(
                    Intent.EXTRA_STREAM,
                    uri
                ) // A content: URI holding a stream of data associated with the Intent, used to supply the data being sent.
                shareIntent.type =
                    "image/jpeg" // The MIME type of the data being handled by this intent.
                startActivity(
                    Intent.createChooser(
                        shareIntent,
                        "Share"
                    )
                )
            }
        }
    }


}
