package com.example.artinstituteapp

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.artinstituteapp.model.Artwork
import com.example.artinstituteapp.util.Constants
import com.example.artinstituteapp.view.ImageActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ImageActivityTest {

    private val testArtwork = Artwork(
        id = 1,
        title = "Test Artwork",
        imageId = "test-image-id"
    )

    private val testImageUrl = "https://test.com/image.jpg"

    private fun launchActivity(): ActivityScenario<ImageActivity> {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ImageActivity::class.java).apply {
            putExtra(Constants.EXTRA_IMAGE_URL, testImageUrl)
            putExtra(Constants.EXTRA_ARTWORK, testArtwork)
        }
        return ActivityScenario.launch(intent)
    }

    @Test
    fun testImageDisplay() {
        launchActivity().use {
            // Verify PhotoView is displayed
            onView(withId(R.id.photoView))
                .check(matches(isDisplayed()))
                .check(matches(withContentDescription(R.string.artwork_image)))

            // Verify progress bar exists but is initially hidden
            onView(withId(R.id.progressBar))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))
        }
    }

    @Test
    fun testScreenRotation() {
        launchActivity().use { scenario ->
            // Verify initial state
            onView(withId(R.id.photoView))
                .check(matches(isDisplayed()))

            // Rotate screen
            scenario.onActivity { activity ->
                activity.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            // Verify PhotoView is still displayed
            onView(withId(R.id.photoView))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testAccessibility() {
        launchActivity().use {
            // Verify content descriptions
            onView(withId(R.id.photoView))
                .check(matches(withContentDescription(R.string.artwork_image)))
        }
    }

    @Test
    fun testErrorState() {
        // Launch with null image URL (should finish activity)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ImageActivity::class.java)
        ActivityScenario.launch<ImageActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assert(activity.isFinishing)
            }
        }
    }
}
