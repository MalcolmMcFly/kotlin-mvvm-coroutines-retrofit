/*
 * Copyright 2017, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lmorda.myapplication.ui

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.core.internal.deps.guava.base.Preconditions.checkArgument
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.util.Log
import android.view.View
import android.widget.ListView
import com.example.lmorda.myapplication.TestUtils.rotateOrientation
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainScreenTest {

    private val TITLE1 = "retrofit"
    private val TITLE2 = "okhttp"
    private val TITLE3 = "glide"

    @Rule
    @JvmField
    var reposActivityTestRule = object :
            ActivityTestRule<MainActivity>(MainActivity::class.java) {
        
    }

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
    fun openApp_showAllTasks() {
        // Opening app and fetching network data will display repos
        fakeIdlingResource(3000)
        onView(withItemText(TITLE1)).check(matches(isDisplayed()))
        onView(withItemText(TITLE2)).check(matches(isDisplayed()))
        onView(withItemText(TITLE3)).check(matches(isDisplayed()))
    }

    @Test
    fun orientationChange_stillShowsRepos() {
        // Rotate device to verify network call still succeeds when it returns
        rotateOrientation(reposActivityTestRule.activity)
        fakeIdlingResource(3000)
        onView(withItemText(TITLE1)).check(matches(isDisplayed()))
    }

    //TODO: EspressoIdlingResource that works with Kotlin coroutines
    private fun fakeIdlingResource(milliseconds: Int) {
        try {
            Thread.sleep(milliseconds.toLong())
        } catch (ex: Exception) {
            Log.e("repos", ex.message)
        }

    }
}