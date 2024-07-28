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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_screen)

        // Dummy data for testing
        val shows = listOf(
            Show(1, "Breaking Bad", "Watching", 4.5f, "Great show!"),
            Show(2, "Game of Thrones", "Finished", 5.0f, "Amazing!"),
            Show(3, "Stranger Things", "Planning", 0.0f, null)
        )

        showAdapter = ShowAdapter(shows) { show ->
            // Handle delete action
            Toast.makeText(this, "${show.name} deleted", Toast.LENGTH_SHORT).show()
        }

        recyclerView = findViewById(R.id.recyclerViewShows)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = showAdapter

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Already on Home screen, do nothing
                    true
                }
                R.id.navigation_search -> {
                    // Navigate to Search screen
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
    }
}
