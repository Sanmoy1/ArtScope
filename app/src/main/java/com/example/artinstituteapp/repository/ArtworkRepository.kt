package com.example.artinstituteapp.repository

import com.example.artinstituteapp.model.Artwork
import com.example.artinstituteapp.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Repository class that handles all data operations related to artworks.
 * This class serves as a single source of truth for the data layer.
 */
class ArtworkRepository {
    private val api = RetrofitClient.instance

    /**
     * Search for artworks based on a query string.
     * Returns a Flow that emits a Result containing either a list of Artwork or an Exception.
     *
     * @param query The search query
     * @param page The page number for pagination
     * @return Flow<Result<List<Artwork>>>
     */
    fun searchArtworks(query: String, page: Int = 1): Flow<Result<List<Artwork>>> = flow {
        try {
            val response = api.searchArtworks(query = query, page = page)
            if (response.isSuccessful) {
                val artworks = response.body()?.artworks?.map { it.toArtwork() } ?: emptyList()
                emit(Result.success(artworks))
            } else {
                emit(Result.failure(Exception("API Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Get detailed information about a specific artwork by ID.
     * Returns a Flow that emits a Result containing either an Artwork or an Exception.
     *
     * @param id The artwork ID
     * @return Flow<Result<Artwork>>
     */
    fun getArtworkById(id: Int): Flow<Result<Artwork>> = flow {
        try {
            val response = api.getArtworkById(id)
            if (response.isSuccessful) {
                val artwork = response.body()?.data?.toArtwork()
                if (artwork != null) {
                    emit(Result.success(artwork))
                } else {
                    emit(Result.failure(Exception("Artwork not found")))
                }
            } else {
                emit(Result.failure(Exception("API Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    companion object {
        @Volatile
        private var instance: ArtworkRepository? = null

        /**
         * Returns the singleton instance of ArtworkRepository.
         */
        fun getInstance(): ArtworkRepository {
            return instance ?: synchronized(this) {
                instance ?: ArtworkRepository().also { instance = it }
            }
        }
    }
}
