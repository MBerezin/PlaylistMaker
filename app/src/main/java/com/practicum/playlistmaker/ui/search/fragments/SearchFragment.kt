package com.practicum.playlistmaker.ui.search.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.ui.search.adapters.TrackAdapter
import com.practicum.playlistmaker.ui.search.adapters.TrackHistoryAdapter
import com.practicum.playlistmaker.ui.search.model.ViewModelSearchState
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel()

    private var searchText: String = String()

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    private val adapter = TrackAdapter({ track ->
        if (clickDebounce()) {
            historyAdapter.setTracks(viewModel.addHistoryTrack (track))
            viewModel.openPlayer(track)
        }

    })
    private val historyAdapter = TrackHistoryAdapter({ track ->
        viewModel.openPlayer(track)
    })


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeSearchState().observe(this.viewLifecycleOwner){searchState ->
            when(searchState){
                ViewModelSearchState.EmptySearchState -> {
                    binding.linearLayoutPlaceholder.visibility = View.VISIBLE
                    binding.textViewPlaceholder.text = getString(R.string.no_results)
                    binding.imageViewPlaceholder.setImageResource(R.drawable.noresults)
                }
                ViewModelSearchState.ErrorSearchState -> {
                    binding.progressBar.visibility = View.GONE
                    binding.linearLayoutPlaceholder.visibility = View.VISIBLE
                    binding.buttonReload.visibility = View.VISIBLE
                    binding.textViewPlaceholder.text = getString(R.string.connection_problem)
                        .plus("\n")
                        .plus(getString(R.string.loading_fail))
                    binding.imageViewPlaceholder.setImageResource(R.drawable.connectionproblem)
                }
                is ViewModelSearchState.SuccessSearchState -> {
                    binding.linearLayoutPlaceholder.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.trackList.visibility = View.VISIBLE
                    adapter.setTracks(searchState.tracks)
                }
                ViewModelSearchState.StartSearchState -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.linearLayoutPlaceholder.visibility = View.GONE
                    binding.trackList.visibility = View.GONE
                    adapter.clearTracks()
                }
            }
        }

        historyAdapter.setTracks(viewModel.readHistoryTracks())

        binding.trackList.layoutManager = LinearLayoutManager(requireContext())
        binding.trackList.adapter = adapter

        binding.historyTrackList.layoutManager = LinearLayoutManager(requireContext())
        binding.historyTrackList.adapter = historyAdapter

        binding.buttonReload.setOnClickListener {
            adapter.clearTracks()
            viewModel.searchTracks()
        }

        binding.buttonLearHistory.setOnClickListener {
            viewModel.clearHistoryTracks()
            historyAdapter.clearTracks()
            binding.linearLayoutHistory.visibility = View.GONE
        }

        binding.imgClear.setOnClickListener {
            binding.etSearch.setText("")
            val inputMethodManager = requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
            adapter.clearTracks()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                viewModel.onTextChanged(s.toString())
                binding.imgClear.isVisible = !s.isNullOrEmpty()

                binding.linearLayoutHistory.visibility =
                    if (binding.etSearch.hasFocus() && s?.isEmpty() === true && historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
                viewModel.searchDebounce()

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.etSearch.addTextChangedListener(textWatcher)

        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->
            binding.linearLayoutHistory.visibility =
                if (hasFocus && binding.etSearch.text.isEmpty() && historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

}