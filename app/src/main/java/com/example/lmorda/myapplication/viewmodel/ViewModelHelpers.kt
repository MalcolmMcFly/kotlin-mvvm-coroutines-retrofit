package com.example.lmorda.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lmorda.myapplication.Injection
import com.example.lmorda.myapplication.repository.ReposRepository

fun getViewModelFactory(): ViewModelFactory {
    val repository = Injection.provideReposRepository()
    return ViewModelFactory(repository)
}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
        private val reposRepository: ReposRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>) =
        with(modelClass) {
            when {
                isAssignableFrom(ReposViewModel::class.java) ->
                    ReposViewModel(reposRepository)
                else ->
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        } as T
}