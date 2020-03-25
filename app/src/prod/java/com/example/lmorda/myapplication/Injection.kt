package com.example.lmorda.myapplication

import com.example.lmorda.myapplication.api.ReposRemoteDataSource
import com.example.lmorda.myapplication.repository.ReposRepository

object Injection {

    fun provideReposRepository(): ReposRepository {
        return ReposRepository.getInstance(ReposRemoteDataSource.newInstance())
    }

}