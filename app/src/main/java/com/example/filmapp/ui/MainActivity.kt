package com.example.filmapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.filmapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.setDecorFitsSystemWindows(false)
        window.setStatusBarContrastEnforced(false)
        window.setNavigationBarContrastEnforced(false)

        //val viewModel: TestViewModel = ViewModelProvider(this)[TestViewModel::class.java]
    }
}