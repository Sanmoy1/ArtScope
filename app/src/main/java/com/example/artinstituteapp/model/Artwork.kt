package com.example.artinstituteapp.model

import android.os.Parcelable
import com.example.artinstituteapp.util.Constants
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * Data class representing an artwork from the Art Institute of Chicago API.
 * This class is Parcelable to allow passing artwork objects between activities.
 */
@Parcelize
data class Artwork(
    val id: Int = 0,
    val title: String = "",
    val artistDisplay: String = "",
    val imageId: String? = null,
    val dateDisplay: String = "",
    val mediumDisplay: String = "",
    val dimensions: String = "",
    val artworkTypeTitle: String = "",
    val departmentTitle: String = "",
    val galleryTitle: String? = null,
    val galleryId: Int? = null,
    val creditLine: String = "",
    val placeOfOrigin: String? = null,
    val apiLink: String = ""
) : Parcelable {
    /**
     * Returns the full image URL for this artwork.
     * The Art Institute API requires constructing the URL from the image ID.
     */
    fun getFullImageUrl(): String {
        return imageId?.let { id ->
            "${Constants.IMAGE_BASE_URL}$id/full/${Constants.DEFAULT_IMAGE_SIZE}${Constants.DEFAULT_IMAGE_QUALITY}.${Constants.DEFAULT_IMAGE_FORMAT}"
        } ?: ""
    }
    
    /**
     * Returns the thumbnail image URL for this artwork.
     */
    fun getThumbnailUrl(): String {
        return imageId?.let { id ->
            "${Constants.IMAGE_BASE_URL}$id/full/${Constants.THUMBNAIL_SIZE}${Constants.DEFAULT_IMAGE_QUALITY}.${Constants.DEFAULT_IMAGE_FORMAT}"
        } ?: ""
    }
}
