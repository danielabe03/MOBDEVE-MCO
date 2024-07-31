package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s12.abe.daniel.mco3.adapters.TVShowAdapter
import com.mobdeve.s12.abe.daniel.mco3.database.DatabaseHelper
import com.mobdeve.s12.abe.daniel.mco3.models.TVShowResponse
import com.mobdeve.s12.abe.daniel.mco3.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchAndAddShowActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tvShowAdapter: TVShowAdapter
    private lateinit var dbHelper: DatabaseHelper
    private var customListId: Int = -1
    private var userId: Int = -1  // Declare userId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.searchandadd_customlist)

        dbHelper = DatabaseHelper(this)
        customListId = intent.getIntExtra("CUSTOM_LIST_ID", -1)
        userId = intent.getIntExtra("USER_ID", -1)  // Get userId from intent

        val etSearch: EditText = findViewById(R.id.etSearch)
        val btnSearch: Button = findViewById(R.id.btnSearch)
        recyclerView = findViewById(R.id.recyclerViewSearchResults)

        recyclerView.layoutManager = LinearLayoutManager(this)
        tvShowAdapter = TVShowAdapter { show ->
            val rating = dbHelper.getReviews(userId) // Fetch the actual rating
                .firstOrNull { it.getAsString("show_name") == show.name }
                ?.getAsFloat("rating") ?: 0f
            dbHelper.addShowToCustomList(customListId, show.name, rating)  // Use actual rating if available
            setResult(RESULT_OK)
            finish()
        }
        recyclerView.adapter = tvShowAdapter

        btnSearch.setOnClickListener {
            val query = etSearch.text.toString()
            if (query.isNotEmpty()) {
                searchTVShows(query)
            }
        }
    }

    private fun searchTVShows(query: String) {
        Log.d("SearchAndAddShowActivity", "Searching for TV shows with query: $query")
        RetrofitInstance.api.searchShows(query).enqueue(object : Callback<List<TVShowResponse>> {
            override fun onResponse(call: Call<List<TVShowResponse>>, response: Response<List<TVShowResponse>>) {
                if (response.isSuccessful) {
                    val tvShows = response.body()?.map { it.show } ?: emptyList()
                    tvShowAdapter.updateTVShows(tvShows)
                } else {
                    Log.e("SearchAndAddShowActivity", "Response not successful")
                }
            }

            override fun onFailure(call: Call<List<TVShowResponse>>, t: Throwable) {
                Log.e("SearchAndAddShowActivity", "Failed to fetch TV shows", t)
            }
        })
    }
}
