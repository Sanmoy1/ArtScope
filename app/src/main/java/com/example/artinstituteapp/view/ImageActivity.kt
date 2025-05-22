package com.example.artinstituteapp.view

import android.os.Bundle
import android.view.MenuItem
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.artinstituteapp.R
import com.example.artinstituteapp.model.Artwork
import com.example.artinstituteapp.util.Constants
import com.example.artinstituteapp.util.hide
import com.example.artinstituteapp.util.loadImage
import com.example.artinstituteapp.util.show
import com.github.chrisbanes.photoview.PhotoView

class ImageActivity : AppCompatActivity() {
    private lateinit var photoView: PhotoView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        // Initialize views
        setupViews()

        // Get image URL and artwork from intent
        val imageUrl = intent.getStringExtra(Constants.EXTRA_IMAGE_URL)
        val artwork = intent.getParcelableExtra<Artwork>(Constants.EXTRA_ARTWORK)

        // Set action bar title
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            title = artwork?.title ?: getString(R.string.artwork_image)
        }

        // Load and display the image
        if (imageUrl != null) {
            progressBar.show()
            photoView.loadImage(imageUrl) { 
                progressBar.hide()
            }
        } else {
            finish()
        }
    }

    private fun setupViews() {
        photoView = findViewById(R.id.photoView)
        progressBar = findViewById(R.id.progressBar)

        // Configure PhotoView
        photoView.apply {
            maximumScale = 10f
            mediumScale = 3f
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
