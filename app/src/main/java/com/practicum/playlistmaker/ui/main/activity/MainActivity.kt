package com.practicum.playlistmaker.ui.main.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityMainBinding
import com.practicum.playlistmaker.ui.main.router.NavigationRouter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationRouter: NavigationRouter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationRouter = Creator.getNavigationRouter(this)

        binding.searchBtn.setOnClickListener {
            navigationRouter.getSearch()
        }

        binding.mediaBtn.setOnClickListener{
            navigationRouter.getMedia()
        }

        binding.settingsBtn.setOnClickListener {
            navigationRouter.getSettings()
        }

    }
}