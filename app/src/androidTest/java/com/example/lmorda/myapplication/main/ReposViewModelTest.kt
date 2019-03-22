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
package com.example.lmorda.myapplication.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.lmorda.myapplication.data.FakeReposRemoteDataSource
import com.example.lmorda.myapplication.repository.ReposRepository
import com.example.lmorda.myapplication.test.util.captureValues
import com.example.lmorda.myapplication.viewmodel.ReposViewModel
import com.example.lmorda.myapplication.vo.Repo
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before

@RunWith(JUnit4::class)
class ReposViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupReposViewModel() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testViewModelRefreshRepos() {
        val repository = ReposRepository(FakeReposRemoteDataSource.getInstance())
        runBlocking {

            // Constructor calls refresh repos, shows and hides spinner
            val reposViewModel = ReposViewModel(repository)
            reposViewModel.spinner.captureValues {
                assertSendsValues(2_000, true, false)
            }

            // Refresh repos from a swipe down, shows and hides spinner, gets mock repo list
            reposViewModel.refreshRepos()
            reposViewModel.spinner.captureValues {
                assertSendsValues(2_000, true, false)
            }
            reposViewModel.repos.captureValues {
                assertSendsValues(2_000, listOf(
                        Repo("1", "retrofit",
                                "Type-safe HTTP client for Android and Java by Square, Inc.",
                                "https://github.com/square/retrofit"),
                        Repo("2", "okhttp",
                                "An HTTP+HTTP/2 client for Android and Java applications.",
                                "https://github.com/square/okhttp"),
                        Repo("3", "glide",
                                "An image loading and caching library for Android focused on smooth scrolling",
                                "https://github.com/bumptech/glide")))
            }
        }
    }

}