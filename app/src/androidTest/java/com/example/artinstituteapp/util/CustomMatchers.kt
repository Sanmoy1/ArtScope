package com.example.artinstituteapp.util

import android.view.View
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object CustomMatchers {
    fun hasMinimumTouchTargetSize(): Matcher<View> {
        return object : BoundedMatcher<View, View>(View::class.java) {
            override fun describeTo(description: Description) {
                description.appendText("has minimum touch target size of 48dp")
            }

            override fun matchesSafely(view: View): Boolean {
                val minSize = 48 * view.resources.displayMetrics.density
                return view.width >= minSize && view.height >= minSize
            }
        }
    }
}
