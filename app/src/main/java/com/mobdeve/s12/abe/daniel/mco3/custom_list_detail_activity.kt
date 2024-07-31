package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobdeve.s12.abe.daniel.mco3.adapters.ShowAdapter
import com.mobdeve.s12.abe.daniel.mco3.database.DatabaseHelper
import com.mobdeve.s12.abe.daniel.mco3.models.Show
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CustomListDetailActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var showAdapter: ShowAdapter
    private val shows = mutableListOf<Show>()
    private lateinit var dbHelper: DatabaseHelper
    private var customListId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_list_detail)

        dbHelper = DatabaseHelper(this)
        customListId = intent.getIntExtra("CUSTOM_LIST_ID", -1)

        recyclerView = findViewById(R.id.recyclerViewCustomListDetail)
        recyclerView.layoutManager = LinearLayoutManager(this)
        showAdapter = ShowAdapter(shows) { show ->
            dbHelper.deleteShowFromCustomList(customListId, show.id)
            loadShows()
        }
        recyclerView.adapter = showAdapter

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, SearchAndAddShowActivity::class.java)
            intent.putExtra("CUSTOM_LIST_ID", customListId)
            startActivity(intent)
        }

        loadShows()

        // Setup BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_search -> {
                    startActivity(Intent(this, SearchActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun loadShows() {
        shows.clear()
        shows.addAll(dbHelper.getShowsInCustomList(customListId))
        showAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        loadShows()
    }
}
