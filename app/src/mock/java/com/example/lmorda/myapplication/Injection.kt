package com.example.lmorda.myapplication

import com.example.lmorda.myapplication.data.FakeReposRemoteDataSource
import com.example.lmorda.myapplication.repository.ReposRepository

object Injection {

    fun provideReposRepository(): ReposRepository {
        return ReposRepository.getInstance(FakeReposRemoteDataSource.getInstance())
    }
}