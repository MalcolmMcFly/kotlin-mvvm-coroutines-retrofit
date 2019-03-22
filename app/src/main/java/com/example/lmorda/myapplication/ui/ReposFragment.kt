package com.example.lmorda.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.lmorda.myapplication.Injection
import com.example.lmorda.myapplication.R
import com.example.lmorda.myapplication.viewmodel.ReposViewModel
import com.example.lmorda.myapplication.vo.Repo
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_countries.*

class ReposFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_countries, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        countries_list.adapter = ReposAdapter(emptyList())

        // activity.run allows us to share viewmodels between fragments
        val viewModel = activity!!.run {
            ViewModelProviders.of(this,
                    ReposViewModel.FACTORY(Injection.provideReposRepository())).get(ReposViewModel::class.java)
        }

        activity!!.findViewById<ScrollChildSwipeRefreshLayout>(R.id.refresh_countries_layout).apply {
            setColorSchemeColors(
                    ContextCompat.getColor(context, R.color.colorPrimary),
                    ContextCompat.getColor(context, R.color.colorAccent),
                    ContextCompat.getColor(context, R.color.colorPrimaryDark)
            )
            scrollUpChild = countries_list
            setOnRefreshListener { viewModel.refreshRepos() }
        }


        // Update the repos when the [ReposViewModel.repos] change
        // Using viewLifecycleOwner to avoid leaking livedata observers in fragments
        viewModel.repos.observe(viewLifecycleOwner, Observer { value ->
            value?.let {
                (countries_list.adapter as ReposAdapter).replaceData(it)
            }
        })

        // show the spinner when [ReposViewModel.spinner] is true
        viewModel.spinner.observe(viewLifecycleOwner, Observer { value ->
            value?.let { show ->with(refresh_countries_layout) {
                    post { isRefreshing = show }
                }
            }
        })

        /**
         * Listener for retry click.
         */
        val retryListener: View.OnClickListener = View.OnClickListener {
            viewModel.refreshRepos()
        }

        // Show a snackbar whenever the [ReposViewModel.snackbar] is updated to a non-null value
        viewModel.snackbar.observe(viewLifecycleOwner, Observer { text ->
            text?.let {
                Snackbar.make(countriesLL, text, Snackbar.LENGTH_SHORT).setAction(R.string.retry_api_call, retryListener).show()
                viewModel.onSnackbarShown()
            }
        })

    }

    /**
     * Adapter for the list of repos
     */
    class ReposAdapter(repos: List<Repo>) : BaseAdapter() {

        override fun getCount() = repos.size
        override fun getItem(i: Int) = repos[i]
        override fun getItemId(i: Int) = i.toLong()

        var repos: List<Repo> = repos
            set(repos) {
                field = repos
                notifyDataSetChanged()
            }

        fun replaceData(newRepos: List<Repo>) {
            repos = newRepos
            notifyDataSetChanged()
        }

        override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
            val repo = getItem(i)
            val rowView = view ?: LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.repo_item, viewGroup, false)
            with(rowView.findViewById<TextView>(R.id.title)) {
                text = repo.name
            }
            with(rowView.findViewById<TextView>(R.id.description)) {
                text = repo.description
            }
            return rowView
        }

    }

    companion object {
        fun newInstance() = ReposFragment()
    }
}