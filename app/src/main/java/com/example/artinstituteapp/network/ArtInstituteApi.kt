package com.example.artinstituteapp.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit interface for the Art Institute of Chicago API.
 * Defines the endpoints and methods for interacting with the API.
 */
interface ArtInstituteApi {
    
    /**
     * Search for artworks based on a query string.
     * 
     * @param query The search query
     * @param fields Comma-separated list of fields to include in the response
     * @param limit Maximum number of results to return
     * @param page Page number for pagination
     * @return Response containing the search results
     */
    @GET("artworks/search")
    suspend fun searchArtworks(
        @Query("q") query: String,
        @Query("fields") fields: String = FIELDS,
        @Query("limit") limit: Int = 15,
        @Query("page") page: Int = 1
    ): Response<ArtworkResponse>
    
    /**
     * Get detailed information about a specific artwork by ID.
     * 
     * @param id The artwork ID
     * @param fields Comma-separated list of fields to include in the response
     * @return Response containing the artwork details
     */
    @GET("artworks/{id}")
    suspend fun getArtworkById(
        @retrofit2.http.Path("id") id: Int,
        @Query("fields") fields: String = FIELDS
    ): Response<ArtworkDetailResponse>
    
    companion object {
        /**
         * Default fields to request from the API.
         * These fields cover all the information we need for displaying artwork details.
         */
        const val FIELDS = "id,title,artist_display,image_id,date_display,medium_display," +
                "dimensions,artwork_type_title,department_title,gallery_title,gallery_id," +
                "credit_line,place_of_origin,api_link"
    }
}

/**
 * Data class representing the response from the Art Institute API's artwork detail endpoint.
 */
data class ArtworkDetailResponse(
    val data: ArtworkDto
)
