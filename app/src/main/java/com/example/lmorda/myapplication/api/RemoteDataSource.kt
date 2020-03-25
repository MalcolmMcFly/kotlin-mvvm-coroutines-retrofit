package com.example.lmorda.myapplication.api

import com.example.lmorda.myapplication.repository.Result
import com.example.lmorda.myapplication.vo.Repo

interface RemoteDataSource {

    suspend fun getRepos(): Result<List<Repo>>

}