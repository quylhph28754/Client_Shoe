package com.fpoly.shoes_app.utility

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.ImageView
import androidx.core.content.ContextCompat
import coil.load
import com.fpoly.shoes_app.R
import java.security.MessageDigest
import java.text.NumberFormat
import java.util.Locale

fun ImageView.loadImage(imgResource: Int? = null) {
    this.load(imgResource) {
        placeholder(R.color.primary_white)
        error(R.color.primary_white)
    }
}

fun ImageView.loadImage(imageUrl: String? = null) {
    this.load(imageUrl) {
        placeholder(R.color.primary_white)
        error(R.color.primary_white)
    }
}
fun Context.getBitmapFromDrawable(drawableResId: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(this, drawableResId)
    val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun String.toMD5(): String {
    val bytes = this.toByteArray()
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}

fun String.formatToVND(): String {
    val number = this.toLongOrNull() ?: return "Invalid number"
    val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return format.format(number)
}