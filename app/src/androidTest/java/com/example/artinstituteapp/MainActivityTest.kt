package com.example.artinstituteapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.artinstituteapp.util.CustomMatchers.hasMinimumTouchTargetSize
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    fun testMainActivityLayout() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Verify toolbar is displayed
            onView(withId(R.id.toolbar))
                .check(matches(isDisplayed()))
                .check(matches(hasDescendant(withText(R.string.app_name))))

            // Verify search input is displayed and functional
            onView(withId(R.id.searchEditText))
                .check(matches(isDisplayed()))
                .check(matches(withHint(R.string.search_hint)))
                .perform(click())
                .perform(typeText("monet"), closeSoftKeyboard())

            // Verify RecyclerView is displayed
            onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()))

            // Verify progress bar exists but is initially hidden
            onView(withId(R.id.progressBar))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))

            // Verify error text exists but is initially hidden
            onView(withId(R.id.errorText))
                .check(matches(withEffectiveVisibility(Visibility.GONE)))
        }
    }

    @Test
    fun testSearchFunctionality() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Test empty search
            onView(withId(R.id.searchEditText))
                .perform(typeText(""), closeSoftKeyboard())

            // Test short search (should not trigger search)
            onView(withId(R.id.searchEditText))
                .perform(clearText())
                .perform(typeText("a"), closeSoftKeyboard())

            // Test valid search
            onView(withId(R.id.searchEditText))
                .perform(clearText())
                .perform(typeText("van gogh"), closeSoftKeyboard())
        }
    }

    @Test
    fun testAccessibility() {
        ActivityScenario.launch(MainActivity::class.java).use {
            // Verify content descriptions
            onView(withId(R.id.searchEditText))
                .check(matches(withContentDescription(R.string.cd_search_artwork)))

            // Verify important touch targets meet minimum size
            onView(withId(R.id.searchEditText))
                .check(matches(hasMinimumTouchTargetSize()))
        }
    }

    @Test
    fun testScreenRotation() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            // Type search query
            onView(withId(R.id.searchEditText))
                .perform(typeText("monet"), closeSoftKeyboard())

            // Rotate screen
            scenario.onActivity { activity ->
                activity.requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }

            // Verify search text is preserved
            onView(withId(R.id.searchEditText))
                .check(matches(withText("monet")))

            // Verify RecyclerView is still displayed
            onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()))
        }
    }
}
