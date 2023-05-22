package com.practicum.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val PLAYER_TRACK = "player_track"

class SearchActivity : AppCompatActivity() {

    private companion object {
        var searchText: String = String()
    }

    private val itunesBaseUrl = "http://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesApi::class.java)

    private lateinit var inpEditText: EditText
    private lateinit var imgClear: ImageView
    private lateinit var recycler: RecyclerView
    private lateinit var historyRecycler: RecyclerView
    private lateinit var linearLayoutPlaceholder: LinearLayout
    private lateinit var linearLayoutHistory: LinearLayout
    private lateinit var imgPlaceholder: ImageView
    private lateinit var txtPlaceholder: TextView
    private lateinit var btnReload: Button
    private lateinit var btnClearHistory: Button
    private lateinit var sharedPref: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var toolbar: Toolbar



    private val tracks = arrayListOf<Track>()
    private val historyTracks = arrayListOf<Track>()
    private val adapter = TrackAdapter(tracks)
    private val historyAdapter = TrackHistoryAdapter(historyTracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        imgClear = findViewById<ImageView>(R.id.img_clear)
        inpEditText = findViewById<EditText>(R.id.et_search)
        recycler = findViewById<RecyclerView>(R.id.trackList)
        historyRecycler = findViewById<RecyclerView>(R.id.historyTrackList)
        linearLayoutPlaceholder = findViewById<LinearLayout>(R.id.linearLayoutPlaceholder)
        linearLayoutHistory = findViewById<LinearLayout>(R.id.linearLayoutHistory)
        imgPlaceholder = findViewById<ImageView>(R.id.imageViewPlaceholder)
        txtPlaceholder = findViewById<TextView>(R.id.textViewPlaceholder)
        btnReload = findViewById<Button>(R.id.buttonReload)
        btnClearHistory = findViewById<Button>(R.id.buttonСlearРistory)
        toolbar = findViewById<Toolbar>(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        sharedPref = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPref)

        searchHistory.read(historyTracks)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        historyRecycler.layoutManager = LinearLayoutManager(this)
        historyRecycler.adapter = historyAdapter

        btnReload.setOnClickListener{
            searchTracks()
        }

        btnClearHistory.setOnClickListener{
            searchHistory.clear(historyTracks)
            linearLayoutHistory.visibility = View.GONE
        }

        adapter.itemClickListener = { track ->
            searchHistory.add(track, historyTracks)
            historyAdapter.notifyDataSetChanged()
            openPlayer(track)
        }

        historyAdapter.itemClickListener = { track ->
            openPlayer(track)
        }

        imgClear.setOnClickListener {
            inpEditText.setText("")
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(inpEditText.windowToken, 0)
            tracks.clear()
            adapter.notifyDataSetChanged()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchText = s.toString()
                imgClear.isVisible = !s.isNullOrEmpty()

                linearLayoutHistory.visibility = if (inpEditText.hasFocus() && s?.isEmpty() === true && historyTracks.isNotEmpty()) View.VISIBLE else View.GONE

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inpEditText.addTextChangedListener(textWatcher)

        inpEditText.setOnFocusChangeListener { view, hasFocus ->
            linearLayoutHistory.visibility = if(hasFocus && inpEditText.text.isEmpty() && historyTracks.isNotEmpty()) View.VISIBLE else View.GONE
        }

        inpEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(inpEditText.text.isNotEmpty()){
                    searchTracks()
                }
                true
            }
            false
        }
    }

    private fun setViewAfterSearch(state: TracksResponseState){
        when (state) {
            TracksResponseState.EMPTY -> {
                linearLayoutPlaceholder.visibility = View.VISIBLE
                txtPlaceholder.text = getString(R.string.no_results)
                imgPlaceholder.setImageResource(R.drawable.noresults)
            }
            TracksResponseState.SUCCESS -> {
                linearLayoutPlaceholder.visibility = View.GONE
            }
            TracksResponseState.ERROR -> {
                linearLayoutPlaceholder.visibility = View.VISIBLE
                btnReload.visibility = View.VISIBLE
                txtPlaceholder.text = getString(R.string.connection_problem)
                    .plus("\n")
                    .plus(getString(R.string.loading_fail))
                imgPlaceholder.setImageResource(R.drawable.connectionproblem)
            }
        }
    }

    private fun searchTracks()  {
        itunesService.search(inpEditText.text.toString()).enqueue(object : Callback<TracksResponse>{
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                if (response.code() == 200){
                    tracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()
                    }
                    if (tracks.isEmpty()) {
                        setViewAfterSearch(TracksResponseState.EMPTY)
                    } else {
                        setViewAfterSearch(TracksResponseState.SUCCESS)
                    }
                } else {
                    tracks.clear()
                    setViewAfterSearch(TracksResponseState.ERROR)
                }
            }

            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                tracks.clear()
                setViewAfterSearch(TracksResponseState.ERROR)
            }
        })
    }

    private fun openPlayer(track: Track){
        val playerIntent = Intent(this, PlayerActivity::class.java)
        sharedPref.edit().putString(PLAYER_TRACK, Gson().toJson(track)).apply()
        startActivity(playerIntent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("searchText", searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString("searchText").toString()
        inpEditText.setText(searchText)
    }
}