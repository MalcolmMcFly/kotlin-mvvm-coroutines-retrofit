package com.example.lmorda.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lmorda.myapplication.repository.ReposRepository
import com.example.lmorda.myapplication.repository.Result
import com.example.lmorda.myapplication.vo.Repo
import kotlinx.coroutines.launch

class ReposViewModel(private val reposRepository: ReposRepository) : ViewModel() {

    private val _spinner = MutableLiveData<Boolean>()
    val spinner: LiveData<Boolean> = _spinner

    private val _repos = MutableLiveData<List<Repo>>()
    val repos: LiveData<List<Repo>> = _repos

    private val _snackbar = MutableLiveData<String>()
    val snackbar: LiveData<String> = _snackbar

    fun refreshRepos(forceRefresh: Boolean) {
        viewModelScope.launch {
            if (_repos.value == null || forceRefresh) _spinner.value = true
            when (val result = reposRepository.refreshRepos(forceRefresh)) {
                is Result.Success -> _repos.postValue(result.data)
                is Result.Error -> _snackbar.postValue(result.exception.message)
            }
            _spinner.value = false
        }
    }
}