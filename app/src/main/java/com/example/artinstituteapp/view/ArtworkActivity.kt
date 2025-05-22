package com.example.artinstituteapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.artinstituteapp.R
import com.example.artinstituteapp.model.Artwork
import com.example.artinstituteapp.util.Constants
import com.example.artinstituteapp.util.hide
import com.example.artinstituteapp.util.loadImage
import com.example.artinstituteapp.util.show
import com.example.artinstituteapp.util.showToast
import com.example.artinstituteapp.viewmodel.ArtworkViewModel
import com.github.chrisbanes.photoview.PhotoView

class ArtworkActivity : AppCompatActivity() {
    private val viewModel: ArtworkViewModel by viewModels()
    
    private lateinit var artworkImage: PhotoView
    private lateinit var titleText: TextView
    private lateinit var artistText: TextView
    private lateinit var dateText: TextView
    private lateinit var mediumText: TextView
    private lateinit var dimensionsText: TextView
    private lateinit var galleryText: TextView
    private lateinit var creditLineText: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artwork)
        
        // Initialize views
        setupViews()
        
        // Get artwork from intent
        val artwork = intent.getParcelableExtra<Artwork>(Constants.EXTRA_ARTWORK)
        
        if (artwork != null) {
            displayArtwork(artwork)
        } else {
            // If artwork is null, finish the activity
            showToast("Error loading artwork")
            finish()
        }
        
        // Observe ViewModel state
        observeViewModel()
    }

    private fun setupViews() {
        artworkImage = findViewById(R.id.artworkImage)
        titleText = findViewById(R.id.titleText)
        artistText = findViewById(R.id.artistText)
        dateText = findViewById(R.id.dateText)
        mediumText = findViewById(R.id.mediumText)
        dimensionsText = findViewById(R.id.dimensionsText)
        galleryText = findViewById(R.id.galleryText)
        creditLineText = findViewById(R.id.creditLineText)
        progressBar = findViewById(R.id.progressBar)

        // Set up toolbar
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun displayArtwork(artwork: Artwork) {
        // Load full-size artwork image
        artworkImage.loadImage(artwork.getFullImageUrl())
        
        // Set text fields
        titleText.text = artwork.title
        artistText.text = artwork.artistDisplay
        dateText.text = artwork.dateDisplay
        mediumText.text = artwork.mediumDisplay
        dimensionsText.text = artwork.dimensions
        creditLineText.text = artwork.creditLine

        // Handle gallery information
        if (artwork.galleryTitle != null && artwork.galleryId != null) {
            galleryText.apply {
                text = artwork.galleryTitle
                setOnClickListener {
                    openGalleryInfo(artwork.galleryId)
                }
            }
        } else {
            galleryText.text = getString(R.string.not_on_display)
        }

        // Set up image click listener for full-screen view
        artworkImage.setOnClickListener {
            Intent(this, ImageActivity::class.java).apply {
                putExtra(Constants.EXTRA_IMAGE_URL, artwork.getFullImageUrl())
                putExtra(Constants.EXTRA_ARTWORK, artwork)
                startActivity(this)
            }
        }
    }

    private fun openGalleryInfo(galleryId: Int) {
        val galleryUrl = "${Constants.BASE_URL}galleries/$galleryId"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(galleryUrl))
        startActivity(intent)
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                progressBar.show()
            } else {
                progressBar.hide()
            }
        }

        viewModel.error.observe(this) { error ->
            error?.let { showToast(it) }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
