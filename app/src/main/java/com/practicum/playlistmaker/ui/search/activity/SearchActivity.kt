package com.practicum.playlistmaker.ui.search.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.*
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.ui.search.adapters.TrackAdapter
import com.practicum.playlistmaker.ui.search.adapters.TrackHistoryAdapter
import com.practicum.playlistmaker.ui.search.model.ViewModelSearchState
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModelSearch: SearchViewModel by viewModel()
    private val handler = Handler(Looper.getMainLooper())

    private var searchText: String = String()

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    private val adapter = TrackAdapter({ track ->
        if (clickDebounce()) {
            historyAdapter.setTracks(viewModelSearch.addHistoryTrack (track))
            viewModelSearch.openPlayer(track)
        }

    })
    private val historyAdapter = TrackHistoryAdapter({ track ->
        viewModelSearch.openPlayer(track)
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModelSearch.observeSearchState().observe(this){searchState ->
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

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        historyAdapter.setTracks(viewModelSearch.readHistoryTracks())

        binding.trackList.layoutManager = LinearLayoutManager(this)
        binding.trackList.adapter = adapter

        binding.historyTrackList.layoutManager = LinearLayoutManager(this)
        binding.historyTrackList.adapter = historyAdapter

        binding.buttonReload.setOnClickListener {
            adapter.clearTracks()
            viewModelSearch.searchTracks()
        }

        binding.buttonLearHistory.setOnClickListener {
            viewModelSearch.clearHistoryTracks()
            historyAdapter.clearTracks()
            binding.linearLayoutHistory.visibility = View.GONE
        }

        binding.imgClear.setOnClickListener {
            binding.etSearch.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
            adapter.clearTracks()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                viewModelSearch.onTextChanged(s.toString())
                binding.imgClear.isVisible = !s.isNullOrEmpty()

                binding.linearLayoutHistory.visibility =
                    if (binding.etSearch.hasFocus() && s?.isEmpty() === true && historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
                viewModelSearch.searchDebounce()

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.etSearch.addTextChangedListener(textWatcher)

        binding.etSearch.setOnFocusChangeListener { view, hasFocus ->
            binding.linearLayoutHistory.visibility =
                if (hasFocus && binding.etSearch.text.isEmpty() && historyAdapter.itemCount > 0) View.VISIBLE else View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchText", searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString("searchText").toString()
        binding.etSearch.setText(searchText)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}