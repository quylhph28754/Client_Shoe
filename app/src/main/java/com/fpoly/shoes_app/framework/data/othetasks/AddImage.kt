package com.fpoly.shoes_app.framework.data.module

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.exifinterface.media.ExifInterface
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
    private const val MAX_BASE64_LENGTH = 150000 // Example limit, adjust as needed
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
        startIntent(takePictureIntent)
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

    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true
        val radius = (bitmap.width.coerceAtMost(bitmap.height) / 2).toFloat()
        canvas.drawCircle((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(), radius, paint)
        return output
    }

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
                        val imageBitmap = getBitmapFromUri(uri)
                        val rotatedBitmap = getRotatedBitmap(imageBitmap, uri)
                        val circularBitmap = getCircularBitmap(rotatedBitmap!!) // Lấy hình tròn
                        imageView?.setImageBitmap(circularBitmap)
                        return encodeToBase64(rotatedBitmap)
                    }
                }

                REQUEST_IMAGE_GALLERY -> {
                    val selectedImage = data?.data
                    selectedImage?.let { uri ->
                        val imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                        val circularBitmap = getCircularBitmap(imageBitmap) // Lấy hình tròn
                        imageView?.setImageBitmap(circularBitmap)
                        return encodeToBase64(imageBitmap)
                    }
                }
            }
        }
        return null
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = activity?.contentResolver?.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun getRotatedBitmap(bitmap: Bitmap?, uri: Uri): Bitmap? {
        if (bitmap == null) return null

        val exif = try {
            ExifInterface(activity?.contentResolver?.openInputStream(uri)!!)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

        val rotation = exif?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val rotationInDegrees = when (rotation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            else -> 0f
        }

        return rotateImage(bitmap, rotationInDegrees)
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    private fun encodeToBase64(image: Bitmap): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        image.compress(CompressFormat.JPEG, 70, byteArrayOutputStream) // Nén ảnh với chất lượng 50%
        val byteArray = byteArrayOutputStream.toByteArray()
        var base64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
        var quality = 70
        while (base64.length > MAX_BASE64_LENGTH && quality > 10) {
            quality -= 10
            byteArrayOutputStream.reset()
            image.compress(CompressFormat.JPEG, quality, byteArrayOutputStream)
            base64 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
        }
        return base64
    }
}
