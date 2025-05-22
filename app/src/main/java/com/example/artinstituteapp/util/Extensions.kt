package com.example.artinstituteapp.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso

/**
 * View visibility extensions
 */
fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * Toast extension for Context
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

/**
 * ImageView extension for loading images with Picasso
 */
fun ImageView.loadImage(url: String?, placeholder: Int? = null, error: (() -> Unit)? = null) {
    if (url.isNullOrBlank()) return
    
    Picasso.get().load(url).apply {
        placeholder?.let { placeholder(it) }
        error?.let { error(it) }
        into(this@loadImage)
    }
}

/**
 * String extensions for URL formatting
 */
fun String.toArtworkImageUrl(size: String = Constants.DEFAULT_IMAGE_SIZE): String {
    return "${Constants.IMAGE_BASE_URL}$this/full/$size/0/${Constants.DEFAULT_IMAGE_QUALITY}.${Constants.DEFAULT_IMAGE_FORMAT}"
}

fun String.toThumbnailUrl(): String {
    return this.toArtworkImageUrl(Constants.THUMBNAIL_SIZE)
}

/**
 * Null handling extensions
 */
fun String?.orEmpty(defaultValue: String = ""): String {
    return this ?: defaultValue
}

fun Int?.orZero(): Int {
    return this ?: 0
}

/**
 * List extensions
 */
fun <T> List<T>?.orEmpty(): List<T> {
    return this ?: emptyList()
}
