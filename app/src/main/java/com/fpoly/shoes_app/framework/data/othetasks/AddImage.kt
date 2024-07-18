package com.fpoly.shoes_app.framework.data.othetasks

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.fpoly.shoes_app.R

@SuppressLint("StaticFieldLeak")
object AddImage {
    private const val REQUEST_IMAGE_CAPTURE = 1
    private const val REQUEST_IMAGE_GALLERY = 2
    private var activity: Activity? = null
    private var fragment: Fragment? = null
    var imageUri: Uri? = null

    fun openImageDialog(context: Context, activity: Activity, fragment: Fragment?) {
        AddImage.activity = activity
        AddImage.fragment = fragment

        val options = arrayOf(context.getString(R.string.capture), context.getString(R.string.gallery))
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.selectImg)

        builder.setItems(options) { _, which ->
            when (which) {
                0 -> takePicture()
                1 -> chooseFromGallery()
            }
        }
        builder.show()
    }

    private fun takePicture() {
        try {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            imageUri = createImageUri()
            if (imageUri != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startIntent(takePictureIntent)
            }
        } catch (ex: Exception) {
            println("" + ex)
        }
    }

    private fun createImageUri(): Uri? {
        return try {
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.DISPLAY_NAME, "temp_image_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/")
            }
            activity?.contentResolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        } catch (ex: Exception) {
            println("Error creating image URI: ${ex.message}")
            null
        }
    }

    private fun chooseFromGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startIntent(pickPhotoIntent)
    }

    private fun startIntent(intent: Intent) {
        if (fragment != null) {
            fragment!!.startActivityForResult(intent, getRequestCode(intent))
        } else {
            activity!!.startActivityForResult(intent, getRequestCode(intent))
        }
    }

    private fun getRequestCode(intent: Intent): Int {
        return when (intent.action) {
            MediaStore.ACTION_IMAGE_CAPTURE -> REQUEST_IMAGE_CAPTURE
            Intent.ACTION_PICK -> REQUEST_IMAGE_GALLERY
            else -> throw IllegalArgumentException("Unsupported action")
        }
    }

    fun handleImageSelectionResult(requestCode: Int, resultCode: Int, data: Intent?): Uri? {
        if (resultCode == Activity.RESULT_OK) {
            return when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> imageUri
                REQUEST_IMAGE_GALLERY -> data?.data
                else -> null
            }
        }
        return null
    }
}
