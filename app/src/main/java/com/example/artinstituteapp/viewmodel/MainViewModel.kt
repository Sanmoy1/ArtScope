package com.example.artinstituteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artinstituteapp.model.Artwork
import com.example.artinstituteapp.repository.ArtworkRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

/**
 * ViewModel for the main screen that handles search operations and maintains UI state.
 */
class MainViewModel : ViewModel() {
    private val repository = ArtworkRepository.getInstance()
    private var searchJob: Job? = null

    // UI state for search results
    private val _artworks = MutableLiveData<List<Artwork>>()
    val artworks: LiveData<List<Artwork>> = _artworks

    // UI state for loading indicator
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // UI state for error messages
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    // Current page number for pagination
    private var currentPage = 1

    // Current search query
    private var currentQuery = ""

    /**
     * Search for artworks using the provided query.
     * This cancels any ongoing search and starts a new one.
     *
     * @param query The search query
     * @param resetPage Whether to reset pagination to page 1 (default true)
     */
    fun searchArtworks(query: String, resetPage: Boolean = true) {
        if (query.isBlank()) {
            _error.value = "Please enter a search term"
            return
        }

        // Cancel any ongoing search
        searchJob?.cancel()

        // Reset page if needed
        if (resetPage) {
            currentPage = 1
            _artworks.value = emptyList()
        }

        currentQuery = query
        _isLoading.value = true
        _error.value = null

        searchJob = viewModelScope.launch {
            repository.searchArtworks(query, currentPage)
                .catch { exception ->
                    _error.value = exception.message ?: "An error occurred"
                    _isLoading.value = false
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { artworks ->
                            if (resetPage) {
                                _artworks.value = artworks
                            } else {
                                _artworks.value = _artworks.value.orEmpty() + artworks
                            }
                            _error.value = if (artworks.isEmpty()) "No results found" else null
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
     * Load the next page of results for the current search query.
     */
    fun loadNextPage() {
        if (_isLoading.value == true || currentQuery.isBlank()) return
        currentPage++
        searchArtworks(currentQuery, resetPage = false)
    }

    /**
     * Retry the last failed search operation.
     */
    fun retrySearch() {
        if (currentQuery.isNotBlank()) {
            searchArtworks(currentQuery, resetPage = true)
        }
    }

    /**
     * Clear the current search results and reset the state.
     */
    fun clearSearch() {
        currentQuery = ""
        currentPage = 1
        _artworks.value = emptyList()
        _error.value = null
        _isLoading.value = false
        searchJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}
