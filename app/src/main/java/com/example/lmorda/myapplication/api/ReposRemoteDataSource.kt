package com.example.lmorda.myapplication.api

import com.example.lmorda.myapplication.vo.Repo
import com.example.lmorda.myapplication.repository.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ReposRemoteDataSource private constructor(val githubApiService: GithubApiService): RemoteDataSource {

    private var TRENDING_REPOS_URL = "/search/repositories" +
            "?q=android+language:java+language:kotlin&sort=stars&order=desc"

    override suspend fun getRepos(): Result<List<Repo>> = withContext(Dispatchers.IO) {
        val request = githubApiService.getRepositories(TRENDING_REPOS_URL)
        try { 
            val response = request.await()
            if (response.items.isNotEmpty()) {
                Result.Success(response.items)
            } else {
                Result.Error(RemoteDataNotFoundException())
            }
        }
        catch (ex: HttpException) {
            Result.Error(RemoteDataNotFoundException())
        }
        catch (ex: Throwable) {
            Result.Error(RemoteDataNotFoundException())
        }

    }

    companion object {
        fun newInstance() = ReposRemoteDataSource(GithubApiService.create())
    }
}
