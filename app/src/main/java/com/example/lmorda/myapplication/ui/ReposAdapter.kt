package com.example.lmorda.myapplication.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.lmorda.myapplication.R
import com.example.lmorda.myapplication.vo.Repo

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