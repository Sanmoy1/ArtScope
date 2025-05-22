package com.example.artinstituteapp.util

import android.widget.ImageView
import com.example.artinstituteapp.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

/**
 * Extension function to load images using Picasso with proper error handling and caching.
 *
 * @param url The URL of the image to load
 * @param onLoadComplete Optional callback for when the image loading completes
 */
fun ImageView.loadImage(url: String?, onLoadComplete: (() -> Unit)? = null) {
    if (url.isNullOrBlank()) {
        setImageResource(R.drawable.placeholder_artwork)
        onLoadComplete?.invoke()
        return
    }

    Picasso.get()
        .load(url)
        .placeholder(R.drawable.placeholder_artwork)
        .error(R.drawable.error_artwork)
        .into(this, object : Callback {
            override fun onSuccess() {
                onLoadComplete?.invoke()
            }

            override fun onError(e: Exception) {
                onLoadComplete?.invoke()
            }
        })
}

/**
 * Initialize Picasso with custom configuration.
 * Call this in Application class.
 */
fun initializePicasso() {
    Picasso.get().apply {
        setIndicatorsEnabled(true)  // Enable debug indicators during development
        isLoggingEnabled = true     // Enable logging during development
    }
}
