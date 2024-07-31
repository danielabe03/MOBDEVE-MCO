package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mobdeve.s12.abe.daniel.mco3.adapters.CustomListAdapter
import com.mobdeve.s12.abe.daniel.mco3.database.DatabaseHelper
import com.mobdeve.s12.abe.daniel.mco3.models.CustomList
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CustomListsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var customListAdapter: CustomListAdapter
    private val customLists = mutableListOf<CustomList>()
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_lists)

        dbHelper = DatabaseHelper(this)

        recyclerView = findViewById(R.id.recyclerViewCustomLists)
        recyclerView.layoutManager = LinearLayoutManager(this)
        customListAdapter = CustomListAdapter(customLists,
            onCustomListClick = { customList ->
                val intent = Intent(this, CustomListDetailActivity::class.java)
                intent.putExtra("CUSTOM_LIST_ID", customList.id)
                startActivity(intent)
            },
            onDeleteCustomListClick = { customList ->
                dbHelper.deleteCustomList(customList.id)
                loadCustomLists()
            }
        )
        recyclerView.adapter = customListAdapter

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, CreateCustomListActivity::class.java)
            startActivity(intent)
        }

        loadCustomLists()

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

    private fun loadCustomLists() {
        customLists.clear()
        customLists.addAll(dbHelper.getCustomLists())
        customListAdapter.notifyDataSetChanged()
    }
}
