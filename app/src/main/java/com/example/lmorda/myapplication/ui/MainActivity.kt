package com.example.lmorda.myapplication.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lmorda.myapplication.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val countriesFragment = ReposFragment()
        fragmentTransaction.replace(R.id.contentFrame, countriesFragment)
        fragmentTransaction.commit()
    }

}