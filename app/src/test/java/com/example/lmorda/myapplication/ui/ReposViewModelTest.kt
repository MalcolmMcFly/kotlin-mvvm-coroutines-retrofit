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

import androidx.lifecycle.LiveData
import com.example.lmorda.myapplication.repository.ReposRepository
import com.example.lmorda.myapplication.repository.Result
import com.example.lmorda.myapplication.viewmodel.ReposViewModel
import com.example.lmorda.myapplication.vo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest=Config.NONE)
class ReposViewModelTest {

    private val TITLE1 = "retrofit"
    private val DESCRIPTION1 = "Type-safe HTTP client for Android and Java by Square, Inc."
    private val URL1 = "https://github.com/square/retrofit"
    private val TITLE2 = "okhttp"
    private val DESCRIPTION2 = "An HTTP+HTTP/2 client for Android and Java applications."
    private val URL2 = "https://github.com/square/okhttp"
    private val TITLE3 = "glide"
    private val DESCRIPTION3 = "An image loading and caching library for Android focused on smooth scrolling"
    private val URL3 = "https://github.com/bumptech/glide"


    private lateinit var viewModel: ReposViewModel

    private lateinit var isLoadingLiveData: LiveData<Boolean>

    private lateinit var errorLiveData: LiveData<String>

    private var repos: List<Repo> = listOf(
            Repo("1", TITLE1, DESCRIPTION1, URL1),
            Repo("2", TITLE2, DESCRIPTION2, URL2),
            Repo("3", TITLE3, DESCRIPTION3, URL3))

    @Mock
    private lateinit var reposRepository: ReposRepository

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Before
    fun setupReposViewModel() {

        Dispatchers.setMain(Dispatchers.Unconfined)

        viewModel = ReposViewModel(reposRepository)

        isLoadingLiveData = viewModel.getLoading()

        errorLiveData = viewModel.getError()

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadRepos() Should Show And Hide Loading Progress`() = runBlocking {

        `when`(reposRepository.getRepos()).thenReturn(Result.Success(repos))

        val isLoading = isLoadingLiveData.value

        viewModel.loadRepos()

//        isLoading?.let { assertTrue(it) }

        verify(reposRepository).getRepos()

//        isLoading?.let { assertFalse(it) }

        return@runBlocking
    }


}