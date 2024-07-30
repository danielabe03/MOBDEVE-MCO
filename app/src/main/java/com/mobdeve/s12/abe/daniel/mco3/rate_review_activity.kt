package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class RateReviewActivity : AppCompatActivity() {

    private lateinit var ratingBar: RatingBar
    private lateinit var reviewInput: TextInputLayout
    private lateinit var btnSubmitReview: Button
    private lateinit var statusDropdown: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rate_review_screen)

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
            val review = reviewInput.editText?.text.toString()
            val status = statusDropdown.selectedItem.toString()

            // Save the review details (for simplicity, using SharedPreferences)
            val sharedPref = getSharedPreferences("USER_REVIEWS", MODE_PRIVATE)
            val editor = sharedPref.edit()
            editor.putFloat("${showId}_RATING", rating)
            editor.putString("${showId}_REVIEW", review)
            editor.putString("${showId}_STATUS", status)
            editor.apply()

            // Show toast message
            Toast.makeText(this, "Review submitted", Toast.LENGTH_SHORT).show()

            // Navigate back to home screen and pass the show details
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.putExtra("SHOW_ID", showId)
            mainIntent.putExtra("SHOW_NAME", showName)
            mainIntent.putExtra("SHOW_GENRES", showGenres)
            mainIntent.putExtra("SHOW_SUMMARY", showSummary)
            mainIntent.putExtra("SHOW_IMAGE", showImage)
            startActivity(mainIntent)
        }
    }
}
