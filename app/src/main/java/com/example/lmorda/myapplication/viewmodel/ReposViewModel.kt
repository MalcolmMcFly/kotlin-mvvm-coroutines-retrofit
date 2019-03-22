/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lmorda.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lmorda.myapplication.api.RemoteDataNotFoundException
import com.example.lmorda.myapplication.repository.ReposRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ReposViewModel(private val reposRepository: ReposRepository) : ViewModel() {

    companion object {
        /**
         * Factory for creating [ReposViewModel]
         */
        val FACTORY = singleArgViewModelFactory(::ReposViewModel)
    }

    /**
     * Fetch repos from remote data source only when the fragment is created
     */
    init {
        refreshRepos()
    }

    /**
     * Request a snackbar to display a string.
     */
    private val _snackBar = MutableLiveData<String>()
    val snackbar: LiveData<String>
        get() = _snackBar

    /**
     * Show a loading spinner if true
     */
    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean>
        get() = _spinner

    /**
     * Update repos via this livedata
     */
    val repos = reposRepository.repos

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

    /**
     * Refresh the repos, showing a loading spinner while it refreshes and errors via snackbar
     */
    fun refreshRepos() {
        launchDataLoad {
            reposRepository.refreshRepos()
        }
    }

    /**
     * Helper function to call a data load function with a loading spinner
     * Errors will trigger a snackbar.
     */
    fun launchDataLoad(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                _spinner.value = true
                block()
            }
            catch (error: RemoteDataNotFoundException) {
                _snackBar.value = error.message
            }
            finally {
                _spinner.value = false
            }
        }
    }


}