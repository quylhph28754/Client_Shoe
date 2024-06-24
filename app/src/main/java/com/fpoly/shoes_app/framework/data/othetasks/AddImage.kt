package com.fpoly.shoes_app.framework.data.module

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.fpoly.shoes_app.R
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

@SuppressLint("StaticFieldLeak")
object AddImage {
    private const val REQUEST_IMAGE_CAPTURE = 1
    private const val REQUEST_IMAGE_GALLERY = 2
    private var activity: Activity? = null
    private var fragment: Fragment? = null
    var imageUri: Uri? = null

    fun openImageDialog(context: Context, activity: Activity, fragment: Fragment?) {
        this.activity = activity
        this.fragment = fragment

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
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        imageUri = createImageUri()
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        if (fragment != null) {
            fragment!!.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            activity!!.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun createImageUri(): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            put(MediaStore.Images.Media.DISPLAY_NAME, "temp_image")
        }
        return activity?.contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
    }

    private fun chooseFromGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (fragment != null) {
            fragment!!.startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_GALLERY)
        } else {
            activity!!.startActivityForResult(pickPhotoIntent, REQUEST_IMAGE_GALLERY)
        }
    }

    @SuppressLint("NewApi")
    fun handleImageSelectionResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
        imageView: ImageView?,
        context: Context
    ): String? {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    imageUri?.let { uri ->
                        return processImageUri(uri, imageView, context)
                    }
                }

                REQUEST_IMAGE_GALLERY -> {
                    val selectedImage = data?.data
                    selectedImage?.let { uri ->
                        return processImageUri(uri, imageView, context)
                    }
                }
            }
        }
        return null
    }

    private fun processImageUri(uri: Uri, imageView: ImageView?, context: Context): String {
        val imgByte = getByteUri(context, uri)
        val base64 = Base64.encodeToString(imgByte, Base64.DEFAULT)
        val decodeString = Base64.decode(base64, Base64.DEFAULT)
        val decodeByte = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.size)
        imageView?.setImageBitmap(decodeByte)
        return base64
    }

    private fun getByteUri(context: Context, uri: Uri): ByteArray {
        val inputStream: InputStream?
        val outputStream = ByteArrayOutputStream()
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream!!.read(buf).also { len = it } != -1) {
                outputStream.write(buf, 0, len)
            }
            inputStream.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return outputStream.toByteArray()
    }
}
