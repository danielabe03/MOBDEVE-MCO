package com.mobdeve.s12.abe.daniel.mco3

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobdeve.s12.abe.daniel.mco3.adapters.ShowAdapter
import com.mobdeve.s12.abe.daniel.mco3.database.DatabaseHelper
import com.mobdeve.s12.abe.daniel.mco3.models.Show

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var showAdapter: ShowAdapter
    private val shows = mutableListOf<Show>()
    private val allShows = mutableListOf<Show>() // To keep the original list
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        setSupportActionBar(findViewById(R.id.toolbar))

        recyclerView = findViewById(R.id.recyclerViewShows)
        recyclerView.layoutManager = LinearLayoutManager(this)
        showAdapter = ShowAdapter(shows) { show ->
            // Handle delete action
            Toast.makeText(this, "${show.name} deleted", Toast.LENGTH_SHORT).show()
            shows.remove(show)
            allShows.remove(show)
            showAdapter.notifyDataSetChanged()
            dbHelper.deleteShow(sessionManager.getUserSession(), show.id)
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

        val btnFilterShows: Button = findViewById(R.id.btnFilterShows)
        btnFilterShows.setOnClickListener {
            showFilterDialog()
        }

        val btnCustomLists: Button = findViewById(R.id.btnCustomLists)
        btnCustomLists.setOnClickListener {
            val intent = Intent(this, CustomListsActivity::class.java)
            startActivity(intent)
        }

        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchShows(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null && newText.isNotEmpty()) {
                    searchShows(newText)
                } else {
                    reloadAllShows() // Reload all shows when search bar is empty
                }
                return false
            }
        })

        // Load initial data
        loadReviews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                sessionManager.clearUserSession()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadReviews() {
        val userId = sessionManager.getUserSession()
        if (userId != -1) {
            shows.clear()
            allShows.clear() // Clear the allShows list
            val reviews = dbHelper.getReviews(userId)
            for (review in reviews) {
                val show = Show(
                    review.getAsInteger("show_id"),
                    review.getAsString("show_name"),
                    review.getAsString("status"),
                    review.getAsFloat("rating"),
                    review.getAsString("comment")
                )
                shows.add(show)
                allShows.add(show) // Add to the allShows list
            }
            showAdapter.notifyDataSetChanged()
        }
    }

    private fun searchShows(query: String) {
        val filteredShows = allShows.filter {
            it.name.contains(query, ignoreCase = true)
        }
        showAdapter.updateShows(filteredShows)
    }

    private fun reloadAllShows() {
        showAdapter.updateShows(allShows)
    }

    private fun showFilterDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.home_filter, null)
        val radioGroupStatus = dialogView.findViewById<RadioGroup>(R.id.radioGroupStatus)
        val radioGroupRating = dialogView.findViewById<RadioGroup>(R.id.radioGroupRating)

        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setTitle("Filter Shows")
        builder.setPositiveButton("Apply") { _, _ ->
            val selectedStatusId = radioGroupStatus.checkedRadioButtonId
            val selectedStatus = when (selectedStatusId) {
                R.id.radioWatching -> "Watching"
                R.id.radioFinished -> "Finished"
                R.id.radioPlanning -> "Planning"
                else -> "All"
            }

            val selectedRatingId = radioGroupRating.checkedRadioButtonId
            val minRating = when (selectedRatingId) {
                R.id.radioRating4AndAbove -> 4.0f
                R.id.radioRating3AndAbove -> 3.0f
                R.id.radioRating2AndAbove -> 2.0f
                R.id.radioRating1AndAbove -> 1.0f
                else -> 0.0f
            }

            applyFilters(selectedStatus, minRating)
        }
        builder.setNegativeButton("Cancel", null)
        builder.create().show()
    }

    private fun applyFilters(status: String, minRating: Float) {
        val userId = sessionManager.getUserSession()
        if (userId != -1) {
            shows.clear()
            val reviews = dbHelper.getReviews(userId)
            for (review in reviews) {
                val show = Show(
                    review.getAsInteger("show_id"),
                    review.getAsString("show_name"),
                    review.getAsString("status"),
                    review.getAsFloat("rating"),
                    review.getAsString("comment")
                )
                if ((status == "All" || show.status == status) && show.rating >= minRating) {
                    shows.add(show)
                }
            }
            showAdapter.notifyDataSetChanged()
        }
    }
}
