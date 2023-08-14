package com.practicum.playlistmaker.ui.settings.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgShare.setOnClickListener {
            viewModel.shareApp()
        }

        binding.imgSupport.setOnClickListener {
            viewModel.openSupport()
        }

        binding.imgTermsOfUse.setOnClickListener {
            viewModel.openTerms()
        }

        binding.switchTheme.setOnCheckedChangeListener { _, checked ->
            viewModel.onClickThemeSwitcher(checked)
        }

        viewModel.observeThemeSwitcherChecked().observe(this.viewLifecycleOwner) { isChecked ->
            binding.switchTheme.isChecked = isChecked
        }

    }

}