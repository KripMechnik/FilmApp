package com.example.filmapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.filmapp.R
import com.example.filmapp.ui.list.ListFragment
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        window.setDecorFitsSystemWindows(false)
//        window.setStatusBarContrastEnforced(false)
//        window.setNavigationBarContrastEnforced(false)


        //val viewModel: TestViewModel = ViewModelProvider(this)[TestViewModel::class.java]
    }
}