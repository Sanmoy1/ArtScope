package com.example.artinstituteapp

import android.app.Application
import com.example.artinstituteapp.util.initializePicasso

class ArtInstituteApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Picasso with custom configuration
        initializePicasso()
    }
}
