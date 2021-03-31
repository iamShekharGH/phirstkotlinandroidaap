package com.iamshekhargh.ekappaisabhi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment()
    }

    fun loadFragment() {
//        supportFragmentManager.beginTransaction().add(R.id.mainactiv_fragment_container, PhirstFrag.newInstance()).commit()
    }
}