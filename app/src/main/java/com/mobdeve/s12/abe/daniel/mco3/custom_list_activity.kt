package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mobdeve.s12.abe.daniel.mco3.adapters.CustomListAdapter
import com.mobdeve.s12.abe.daniel.mco3.database.DatabaseHelper
import com.mobdeve.s12.abe.daniel.mco3.models.CustomList

class CustomListsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var customListAdapter: CustomListAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager
    private var customLists = mutableListOf<CustomList>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_lists)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        recyclerView = findViewById(R.id.recyclerViewCustomLists)
        recyclerView.layoutManager = LinearLayoutManager(this)
        customListAdapter = CustomListAdapter(customLists, { customList ->
            val intent = Intent(this, CustomListDetailActivity::class.java)
            intent.putExtra("CUSTOM_LIST_ID", customList.id)
            startActivity(intent)
        }, { customList ->
            dbHelper.deleteCustomList(customList.id)
            loadCustomLists()
        })
        recyclerView.adapter = customListAdapter

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this, CreateCustomListActivity::class.java)
            startActivityForResult(intent, CREATE_CUSTOM_LIST_REQUEST_CODE)
        }

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

        loadCustomLists()
    }

    private fun loadCustomLists() {
        val userId = sessionManager.getUserSession()
        customLists.clear()
        customLists.addAll(dbHelper.getCustomLists(userId))
        customListAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_CUSTOM_LIST_REQUEST_CODE && resultCode == RESULT_OK) {
            loadCustomLists()
        }
    }

    companion object {
        const val CREATE_CUSTOM_LIST_REQUEST_CODE = 1
    }
}
