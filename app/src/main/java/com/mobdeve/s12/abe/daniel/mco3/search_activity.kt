package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobdeve.s12.abe.daniel.mco3.R
import com.mobdeve.s12.abe.daniel.mco3.adapters.TVShowAdapter
import com.mobdeve.s12.abe.daniel.mco3.models.TVShowResponse
import com.mobdeve.s12.abe.daniel.mco3.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvShowAdapter: TVShowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_screen)

        recyclerView = findViewById(R.id.recyclerViewSearchResults)
        recyclerView.layoutManager = LinearLayoutManager(this)
        tvShowAdapter = TVShowAdapter { show ->
            val intent = Intent(this, ShowDetailsActivity::class.java)
            intent.putExtra("SHOW_ID", show.id)
            intent.putExtra("SHOW_NAME", show.name)
            intent.putExtra("SHOW_GENRES", show.genres.joinToString(", "))
            intent.putExtra("SHOW_SUMMARY", show.summary)
            intent.putExtra("SHOW_IMAGE", show.image?.original)
            startActivity(intent)
        }
        recyclerView.adapter = tvShowAdapter

        val searchView = findViewById<androidx.appcompat.widget.SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchTVShows(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_search -> true
                else -> false
            }
        }
    }

    private fun searchTVShows(query: String) {
        Log.d("SearchActivity", "Searching for TV shows with query: $query")
        RetrofitInstance.api.searchShows(query).enqueue(object : Callback<List<TVShowResponse>> {
            override fun onResponse(call: Call<List<TVShowResponse>>, response: Response<List<TVShowResponse>>) {
                if (response.isSuccessful) {
                    val tvShows = response.body()?.map { it.show } ?: emptyList()
                    tvShowAdapter.updateTVShows(tvShows)
                } else {
                    Log.e("SearchActivity", "Response not successful")
                }
            }

            override fun onFailure(call: Call<List<TVShowResponse>>, t: Throwable) {
                Log.e("SearchActivity", "Failed to fetch TV shows", t)
            }
        })
    }
}
