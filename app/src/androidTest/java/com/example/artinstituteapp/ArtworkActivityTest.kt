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
import com.example.artinstituteapp.view.ArtworkActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArtworkActivityTest {

    private val testArtwork = Artwork(
        id = 1,
        title = "Test Artwork",
        artistDisplay = "Test Artist",
        dateDisplay = "2025",
        mediumDisplay = "Oil on canvas",
        dimensions = "100 × 100 cm",
        creditLine = "Test Credit",
        galleryTitle = "Test Gallery",
        galleryId = 1,
        imageId = "test-image-id"
    )

    private fun launchActivity(): ActivityScenario<ArtworkActivity> {
        val intent = Intent(ApplicationProvider.getApplicationContext(), ArtworkActivity::class.java).apply {
            putExtra(Constants.EXTRA_ARTWORK, testArtwork)
        }
        return ActivityScenario.launch(intent)
    }

    @Test
    fun testArtworkDetailsDisplay() {
        launchActivity().use {
            // Verify all text fields are displayed with correct content
            onView(withId(R.id.titleText))
                .check(matches(isDisplayed()))
                .check(matches(withText(testArtwork.title)))

            onView(withId(R.id.artistText))
                .check(matches(isDisplayed()))
                .check(matches(withText(testArtwork.artistDisplay)))

            onView(withId(R.id.dateText))
                .check(matches(isDisplayed()))
                .check(matches(withText(testArtwork.dateDisplay)))

            onView(withId(R.id.mediumText))
                .check(matches(isDisplayed()))
                .check(matches(withText(testArtwork.mediumDisplay)))

            onView(withId(R.id.dimensionsText))
                .check(matches(isDisplayed()))
                .check(matches(withText(testArtwork.dimensions)))

            onView(withId(R.id.creditLineText))
                .check(matches(isDisplayed()))
                .check(matches(withText(testArtwork.creditLine)))

            // Verify gallery information
            onView(withId(R.id.galleryText))
                .check(matches(isDisplayed()))
                .check(matches(withText(testArtwork.galleryTitle)))
                .check(matches(isClickable()))
        }
    }

    @Test
    fun testImageDisplay() {
        launchActivity().use {
            // Verify artwork image is displayed
            onView(withId(R.id.artworkImage))
                .check(matches(isDisplayed()))
                .check(matches(withContentDescription(R.string.artwork_image)))

            // Test image click for full-screen view
            onView(withId(R.id.artworkImage))
                .perform(click())
        }
    }

    @Test
    fun testScreenRotation() {
        launchActivity().use { scenario ->
            // Verify initial state
            onView(withId(R.id.titleText))
                .check(matches(withText(testArtwork.title)))

            // Rotate screen
            scenario.onActivity { activity ->
                activity.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            // Verify content is preserved
            onView(withId(R.id.titleText))
                .check(matches(withText(testArtwork.title)))
            onView(withId(R.id.artworkImage))
                .check(matches(isDisplayed()))
        }
    }

    @Test
    fun testAccessibility() {
        launchActivity().use {
            // Verify content descriptions
            onView(withId(R.id.artworkImage))
                .check(matches(withContentDescription(R.string.artwork_image)))

            // Verify text is readable by screen readers
            onView(withId(R.id.titleText))
                .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
                .check(matches(isFocusable()))

            // Verify clickable elements are accessible
            onView(withId(R.id.galleryText))
                .check(matches(isClickable()))
                .check(matches(isFocusable()))
        }
    }

    @Test
    fun testErrorState() {
        // Launch with null artwork (should finish activity)
        val intent = Intent(ApplicationProvider.getApplicationContext(), ArtworkActivity::class.java)
        ActivityScenario.launch<ArtworkActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assert(activity.isFinishing)
            }
        }
    }
}
