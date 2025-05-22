package com.example.artinstituteapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.artinstituteapp.util.Constants
import com.example.artinstituteapp.util.hide
import com.example.artinstituteapp.util.show
import com.example.artinstituteapp.util.showToast
import com.example.artinstituteapp.view.ArtworkActivity
import com.example.artinstituteapp.view.adapter.ArtworkAdapter
import com.example.artinstituteapp.viewmodel.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var adapter: ArtworkAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize views
        setupViews()
        
        // Setup RecyclerView
        setupRecyclerView()
        
        // Setup search functionality
        setupSearch()
        
        // Observe ViewModel state
        observeViewModel()
    }

    private fun setupViews() {
        searchEditText = findViewById(R.id.searchEditText)
        progressBar = findViewById(R.id.progressBar)
        errorText = findViewById(R.id.errorText)
        recyclerView = findViewById(R.id.recyclerView)
    }

    private fun setupRecyclerView() {
        adapter = ArtworkAdapter { artwork ->
            // Navigate to artwork details
            Intent(this, ArtworkActivity::class.java).apply {
                putExtra(Constants.EXTRA_ARTWORK, artwork)
                startActivity(this)
            }
        }

        recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, Constants.RECYCLER_VIEW_SPAN_COUNT)
            adapter = this@MainActivity.adapter
            
            // Add scroll listener for pagination
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount

                    if (lastVisibleItem == totalItemCount - 1) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener { editable ->
            searchJob?.cancel()
            searchJob = MainScope().launch {
                delay(Constants.SEARCH_DEBOUNCE_TIME)
                editable?.toString()?.let { query ->
                    if (query.length >= Constants.MIN_SEARCH_LENGTH) {
                        viewModel.searchArtworks(query)
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.artworks.observe(this) { artworks ->
            adapter.submitList(artworks)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressBar.show()
                errorText.hide()
            } else {
                progressBar.hide()
            }
        }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                errorText.text = error
                errorText.show()
                showToast(error)
            } else {
                errorText.hide()
            }
        }
    }
}