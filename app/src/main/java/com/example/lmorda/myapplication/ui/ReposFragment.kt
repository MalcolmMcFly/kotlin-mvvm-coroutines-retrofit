package com.example.lmorda.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.lmorda.myapplication.R
import com.example.lmorda.myapplication.viewmodel.ReposViewModel
import com.example.lmorda.myapplication.viewmodel.getViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_countries.*

class ReposFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_countries, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel by viewModels<ReposViewModel> { getViewModelFactory() }

        // Display list of repositories
        countries_list.adapter = ReposAdapter(emptyList())
        activity?.findViewById<ScrollChildSwipeRefreshLayout>(R.id.refresh_countries_layout)?.apply {
            setColorSchemeColors(
                    ContextCompat.getColor(context, R.color.colorPrimary),
                    ContextCompat.getColor(context, R.color.colorAccent),
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
            scrollUpChild = countries_list
            setOnRefreshListener { viewModel.refreshRepos(true) }
        }
        viewModel.repos.observe(viewLifecycleOwner, Observer { repos ->
            repos?.let {
                (countries_list.adapter as ReposAdapter).replaceData(it)
            }
        })

        // Show spinner when loading from API
        viewModel.spinner.observe(viewLifecycleOwner, Observer { value ->
            value?.let { show ->
                with(refresh_countries_layout) {
                    post { isRefreshing = show }
                }
            }
        })

        // Snackbar messages shows error and a retry button
        val retryListener: View.OnClickListener = View.OnClickListener {
            viewModel.refreshRepos(true)
        }
        viewModel.snackbar.observe(viewLifecycleOwner, Observer { text ->
            text?.let {
                Snackbar.make(countriesLL, it, Snackbar.LENGTH_SHORT).setAction(R.string.retry_api_call, retryListener).show()
            }
        })

        // Fetch repos from remote data source only when the fragment is created
        viewModel.refreshRepos(false)
    }

}