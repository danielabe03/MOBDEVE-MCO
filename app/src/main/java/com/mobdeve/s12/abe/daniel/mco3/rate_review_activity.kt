package com.mobdeve.s12.abe.daniel.mco3

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import com.mobdeve.s12.abe.daniel.mco3.database.DatabaseHelper

class RateReviewActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var reviewInput: TextInputLayout
    private lateinit var btnSubmitReview: Button
    private lateinit var statusDropdown: Spinner
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rate_review_screen)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        ratingBar = findViewById(R.id.ratingBar)
        reviewInput = findViewById(R.id.tilReview)
        btnSubmitReview = findViewById(R.id.btnSubmitReview)
        statusDropdown = findViewById(R.id.statusDropdown)

        val showId = intent.getIntExtra("SHOW_ID", -1)
        val showName = intent.getStringExtra("SHOW_NAME")
        val showGenres = intent.getStringExtra("SHOW_GENRES")
        val showSummary = intent.getStringExtra("SHOW_SUMMARY")
        val showImage = intent.getStringExtra("SHOW_IMAGE")

        // Set up status dropdown
        ArrayAdapter.createFromResource(
            this,
            R.array.status_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            statusDropdown.adapter = adapter
        }

        btnSubmitReview.setOnClickListener {
            val rating = ratingBar.rating
            val comment = reviewInput.editText?.text.toString()
            val status = statusDropdown.selectedItem.toString()

            val userId = sessionManager.getUserSession()
            if (userId != -1) {
                val result = dbHelper.addReview(userId, showId, showName, showGenres, showSummary, showImage, rating, comment, status)
                if (result != -1L) {
                    Toast.makeText(this, "Review submitted", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    Toast.makeText(this, "Failed to submit review", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                navigateToLogin()
            }
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navigateToHome()
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

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
