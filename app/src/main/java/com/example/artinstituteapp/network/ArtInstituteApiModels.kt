package com.example.artinstituteapp.network

import com.example.artinstituteapp.model.Artwork
import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response from the Art Institute API's artwork search endpoint.
 */
data class ArtworkResponse(
    @SerializedName("data")
    val artworks: List<ArtworkDto> = emptyList(),
    
    @SerializedName("pagination")
    val pagination: PaginationInfo = PaginationInfo(),
    
    @SerializedName("config")
    val config: ConfigInfo = ConfigInfo()
)

/**
 * Data class representing pagination information from the API response.
 */
data class PaginationInfo(
    @SerializedName("total")
    val total: Int = 0,
    
    @SerializedName("limit")
    val limit: Int = 0,
    
    @SerializedName("offset")
    val offset: Int = 0,
    
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    
    @SerializedName("current_page")
    val currentPage: Int = 0
)

/**
 * Data class representing configuration information from the API response.
 */
data class ConfigInfo(
    @SerializedName("iiif_url")
    val iiifUrl: String = "https://www.artic.edu/iiif/2",
    
    @SerializedName("website_url")
    val websiteUrl: String = "https://www.artic.edu"
)

/**
 * Data Transfer Object for Artwork data from the API.
 * This class maps the API response fields to our domain model.
 */
data class ArtworkDto(
    @SerializedName("id")
    val id: Int = 0,
    
    @SerializedName("title")
    val title: String = "",
    
    @SerializedName("artist_display")
    val artistDisplay: String = "",
    
    @SerializedName("image_id")
    val imageId: String? = null,
    
    @SerializedName("date_display")
    val dateDisplay: String = "",
    
    @SerializedName("medium_display")
    val mediumDisplay: String = "",
    
    @SerializedName("dimensions")
    val dimensions: String = "",
    
    @SerializedName("artwork_type_title")
    val artworkTypeTitle: String = "",
    
    @SerializedName("department_title")
    val departmentTitle: String = "",
    
    @SerializedName("gallery_title")
    val galleryTitle: String? = null,
    
    @SerializedName("gallery_id")
    val galleryId: Int? = null,
    
    @SerializedName("credit_line")
    val creditLine: String = "",
    
    @SerializedName("place_of_origin")
    val placeOfOrigin: String? = null,
    
    @SerializedName("api_link")
    val apiLink: String = ""
) {
    /**
     * Converts the DTO to a domain model Artwork object.
     */
    fun toArtwork(): Artwork {
        return Artwork(
            id = id,
            title = title,
            artistDisplay = artistDisplay,
            imageId = imageId,
            dateDisplay = dateDisplay,
            mediumDisplay = mediumDisplay,
            dimensions = dimensions,
            artworkTypeTitle = artworkTypeTitle,
            departmentTitle = departmentTitle,
            galleryTitle = galleryTitle,
            galleryId = galleryId,
            creditLine = creditLine,
            placeOfOrigin = placeOfOrigin,
            apiLink = apiLink
        )
    }
}
