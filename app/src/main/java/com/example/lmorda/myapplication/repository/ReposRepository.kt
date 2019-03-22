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
package com.example.lmorda.myapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.lmorda.myapplication.api.RemoteDataNotFoundException
import com.example.lmorda.myapplication.api.RemoteDataSource
import com.example.lmorda.myapplication.vo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ReposRepository(val reposRemoteDataSource: RemoteDataSource) {

    /**
     * [LiveData] to load repos.  Repos will be loaded from the repository cache.
     * Observing this will not cause the repos to be refreshed, use [ReposRepository.refreshRepos].
     * This won't be instantiated until the property is used for the first time due to 'by lazy'.
     */
    val repos: MutableLiveData<List<Repo>> by lazy {
        MutableLiveData<List<Repo>>()
    }

    /**
     * Refresh the current repos and save the results to the offline cache.
     * This method does not return new repos. Use [ReposRepository.repos] for that.
     */
    suspend fun refreshRepos() {
        withContext(Dispatchers.IO) {
            try {
                val result = reposRemoteDataSource.getRepos()
                if (result is Result.Success) {
                    repos.postValue(result.data)
                }

            } catch (error: RemoteDataNotFoundException) {
                throw ReposRefreshError(error)
            }
        }
    }
    /**
     * Thrown when there was a error fetching repos
     */
    class ReposRefreshError(cause: Throwable) : Throwable(cause.message, cause)

    companion object {
        private var INSTANCE: ReposRepository? = null

        @JvmStatic
        fun getInstance(remoteDataSource: RemoteDataSource): ReposRepository {
            return INSTANCE ?: ReposRepository(remoteDataSource).apply { INSTANCE = this }
        }

        @JvmStatic
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}