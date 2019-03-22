package com.example.lmorda.myapplication.main

import android.util.Log
import android.view.View
import android.widget.ListView
import androidx.test.espresso.Espresso.onView
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import org.junit.runner.RunWith
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.core.internal.deps.guava.base.Preconditions.checkArgument
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.lmorda.myapplication.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

@RunWith(AndroidJUnit4::class)
@LargeTest
class ReposScreenTest {

    private val TITLE1 = "retrofit"

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    private fun withItemText(itemText: String): Matcher<View> {
        checkArgument(itemText.isNotEmpty(), "itemText cannot be null or empty")
        return object : TypeSafeMatcher<View>() {
            public override fun matchesSafely(item: View): Boolean {
                return allOf(
                        isDescendantOfA(isAssignableFrom(ListView::class.java)),
                        withText(itemText)).matches(item)
            }

            override fun describeTo(description: Description) {
                description.appendText("is isDescendantOfA LV with text " + itemText)
            }
        }
    }

    @Test
    fun openApp_showsRepos() {
        fakeIdlingResource(3000)
        onView(withItemText(TITLE1)).check(matches(isDisplayed()))
    }

    //TODO: EspressoIdlingResource that works with Kotlin coroutines
    private fun fakeIdlingResource(milliseconds: Int) {
        try {
            Thread.sleep(milliseconds.toLong())
        } catch (ex: Exception) {
            Log.e("androidtesting", ex.message)
        }
    }
}