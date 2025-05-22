package com.example.artinstituteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artinstituteapp.model.Artwork
import com.example.artinstituteapp.repository.ArtworkRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel for handling detailed artwork information and state.
 */
class ArtworkViewModel : ViewModel() {
    private val repository = ArtworkRepository.getInstance()

    // UI state for artwork details
    private val _artwork = MutableLiveData<Artwork>()
    val artwork: LiveData<Artwork> = _artwork

    // UI state for loading indicator
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // UI state for error messages
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /**
     * Load artwork details by ID.
     *
     * @param artworkId The ID of the artwork to load
     */
    fun loadArtwork(artworkId: Int) {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            repository.getArtworkById(artworkId)
                .catch { exception ->
                    _error.value = exception.message ?: "An error occurred"
                    _isLoading.value = false
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { artwork ->
                            _artwork.value = artwork
                            _error.value = null
                        },
                        onFailure = { exception ->
                            _error.value = exception.message ?: "An error occurred"
                        }
                    )
                    _isLoading.value = false
                }
        }
    }

    /**
     * Retry loading the artwork details after a failure.
     */
    fun retryLoading() {
        _artwork.value?.let { artwork ->
            loadArtwork(artwork.id)
        }
    }

    /**
     * Clear the current artwork details and reset the state.
     */
    fun clearArtwork() {
        _artwork.value = null
        _error.value = null
        _isLoading.value = false
    }
}
