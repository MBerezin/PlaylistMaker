package com.practicum.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    private lateinit var linearLayoutPlaceholder: LinearLayout
    private lateinit var imgPlaceholder: ImageView
    private lateinit var txtPlaceholder: TextView
    private lateinit var btnReload: Button

    private val tracks = arrayListOf<Track>()
    private val adapter = TrackAdapter(tracks)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        imgClear = findViewById<ImageView>(R.id.img_clear)
        inpEditText = findViewById<EditText>(R.id.et_search)
        recycler = findViewById<RecyclerView>(R.id.trackList)
        linearLayoutPlaceholder = findViewById<LinearLayout>(R.id.linearLayoutPlaceholder)
        imgPlaceholder = findViewById<ImageView>(R.id.imageViewPlaceholder)
        txtPlaceholder = findViewById<TextView>(R.id.textViewPlaceholder)
        btnReload = findViewById<Button>(R.id.buttonReload)



        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        btnReload.setOnClickListener{
            searchTracks()
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
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inpEditText.addTextChangedListener(textWatcher)

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