package com.example.artinstituteapp.util

/**
 * Object containing constant values used throughout the app.
 */
object Constants {
    // API related constants
    const val BASE_URL = "https://api.artic.edu/api/v1/"
    const val IMAGE_BASE_URL = "https://www.artic.edu/iiif/2/"
    const val DEFAULT_PAGE_SIZE = 15
    const val INITIAL_PAGE = 1

    // Image related constants
    const val DEFAULT_IMAGE_SIZE = "843,/0/"
    const val DEFAULT_IMAGE_QUALITY = "default"
    const val DEFAULT_IMAGE_FORMAT = "jpg"
    const val THUMBNAIL_SIZE = "200,/0/"

    // Bundle keys for intent extras
    const val EXTRA_ARTWORK_ID = "artwork_id"
    const val EXTRA_ARTWORK = "artwork"
    const val EXTRA_IMAGE_URL = "image_url"

    // UI related constants
    const val SEARCH_DEBOUNCE_TIME = 500L // milliseconds
    const val MIN_SEARCH_LENGTH = 2
    const val RECYCLER_VIEW_SPAN_COUNT = 2

    // Error messages
    const val ERROR_NO_RESULTS = "No results found"
    const val ERROR_NETWORK = "Network error occurred"
    const val ERROR_GENERAL = "An error occurred"
    const val ERROR_INVALID_SEARCH = "Please enter at least 2 characters"
}
