package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobdeve.s12.abe.daniel.mco3.adapters.ShowAdapter
import com.mobdeve.s12.abe.daniel.mco3.models.Show

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var showAdapter: ShowAdapter
    private val shows = mutableListOf<Show>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        recyclerView = findViewById(R.id.recyclerViewShows)
        recyclerView.layoutManager = LinearLayoutManager(this)
        showAdapter = ShowAdapter(shows) { show ->
            // Handle delete action
            Toast.makeText(this, "${show.name} deleted", Toast.LENGTH_SHORT).show()
            shows.remove(show)
            showAdapter.notifyDataSetChanged()
        }
        recyclerView.adapter = showAdapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> true
                R.id.navigation_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                else -> false
            }
        }

        val mainContainer = findViewById<ConstraintLayout>(R.id.main_container)
        ViewCompat.setOnApplyWindowInsetsListener(mainContainer) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load initial data
        loadDummyData()

        // Check for new reviews
        checkForNewReviews()
    }

    private fun loadDummyData() {
        shows.addAll(listOf(
            Show(1, "Breaking Bad", "Watching", 4.5f, "Great show!"),
            Show(2, "Game of Thrones", "Finished", 5.0f, "Amazing!"),
            Show(3, "Stranger Things", "Planning", 0.0f, null)
        ))
        showAdapter.notifyDataSetChanged()
    }

    private fun checkForNewReviews() {
        val sharedPref = getSharedPreferences("USER_REVIEWS", MODE_PRIVATE)
        for (show in shows) {
            val rating = sharedPref.getFloat("${show.id}_RATING", -1f)
            val review = sharedPref.getString("${show.id}_REVIEW", null)
            val status = sharedPref.getString("${show.id}_STATUS", null)
            if (rating != -1f && review != null && status != null) {
                show.rating = rating
                show.comment = review
                show.status = status
            }
        }
        showAdapter.notifyDataSetChanged()

        // Check if new show is passed from RateReviewActivity
        val showId = intent.getIntExtra("SHOW_ID", -1)
        if (showId != -1) {
            val showName = intent.getStringExtra("SHOW_NAME")
            val showGenres = intent.getStringExtra("SHOW_GENRES")
            val showSummary = intent.getStringExtra("SHOW_SUMMARY")
            val showImage = intent.getStringExtra("SHOW_IMAGE")

            val newShow = Show(
                showId,
                showName ?: "",
                sharedPref.getString("${showId}_STATUS", "Planning") ?: "Planning",
                sharedPref.getFloat("${showId}_RATING", 0.0f),
                sharedPref.getString("${showId}_REVIEW", null)
            )
            shows.add(newShow)
            showAdapter.notifyDataSetChanged()
        }
    }
}
