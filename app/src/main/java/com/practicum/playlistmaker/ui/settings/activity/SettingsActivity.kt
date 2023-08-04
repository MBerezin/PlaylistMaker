package com.practicum.playlistmaker.ui.settings.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModelSettings: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelSettings = ViewModelProvider(
            this,
            SettingsViewModel.getSettingsViewModelFactory()
        )[SettingsViewModel::class.java]

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.imgShare.setOnClickListener {
            viewModelSettings.shareApp()
        }

        binding.imgSupport.setOnClickListener {
            viewModelSettings.openSupport()
        }

        binding.imgTermsOfUse.setOnClickListener {
            viewModelSettings.openTerms()
        }

        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModelSettings.onClickThemeSwitcher(checked)
        }

        viewModelSettings.observeThemeSwitcherChecked().observe(this) { isChecked ->
            binding.switchTheme.isChecked = isChecked
        }

    }
}