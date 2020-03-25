package com.example.lmorda.myapplication.repository

import com.example.lmorda.myapplication.api.RemoteDataSource
import com.example.lmorda.myapplication.vo.Repo

class ReposRepository(val reposRemoteDataSource: RemoteDataSource) {

    private var cachedRepos: List<Repo>? = null

    suspend fun refreshRepos(forceRefresh: Boolean): Result<List<Repo>?> {
        return if (forceRefresh || cachedRepos == null) {
            val result = reposRemoteDataSource.getRepos()
            when {
                result is Result.Success -> cachedRepos = result.data
                else -> cachedRepos = null
            }
            return result
        } else {
            Result.Success(cachedRepos)
        }
    }

    companion object {
        private var INSTANCE: ReposRepository? = null

        fun getInstance(remoteDataSource: RemoteDataSource): ReposRepository {
            return INSTANCE ?: ReposRepository(remoteDataSource).apply { INSTANCE = this }
        }
    }
}