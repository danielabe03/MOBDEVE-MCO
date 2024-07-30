package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobdeve.s12.abe.daniel.mco3.adapters.ShowAdapter
import com.mobdeve.s12.abe.daniel.mco3.models.Show
import com.mobdeve.s12.abe.daniel.mco3.database.DatabaseHelper

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var showAdapter: ShowAdapter
    private val shows = mutableListOf<Show>()
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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
        loadReviews()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Clear the user session and navigate to the login screen
                sessionManager.clearUserSession()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadReviews() {
        val userId = sessionManager.getUserSession()
        if (userId != -1) {
            val reviews = dbHelper.getReviews(userId)
            for (review in reviews) {
                val show = Show(
                    review.getAsInteger("show_id"),
                    review.getAsString("show_name"),
                    review.getAsString("status"),
                    review.getAsFloat("rating"),
                    review.getAsString("review")
                )
                shows.add(show)
            }
            showAdapter.notifyDataSetChanged()
        }
    }
}
