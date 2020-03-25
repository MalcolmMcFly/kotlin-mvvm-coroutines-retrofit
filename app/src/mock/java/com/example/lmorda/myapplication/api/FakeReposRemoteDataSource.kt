package com.example.lmorda.myapplication.api

import com.example.lmorda.myapplication.repository.Result
import com.example.lmorda.myapplication.vo.Repo

class FakeReposRemoteDataSource private constructor(): RemoteDataSource {

    override suspend fun getRepos(): Result<List<Repo>> {
        return Result.Success(listOf(
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

    companion object {

        private lateinit var INSTANCE: FakeReposRemoteDataSource
        private var needsNewInstance = true

        @JvmStatic
        fun getInstance(): FakeReposRemoteDataSource {
            if (needsNewInstance) {
                INSTANCE = FakeReposRemoteDataSource()
                needsNewInstance = false
            }
            return INSTANCE
        }

    }

}