package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView

class ShowDetailsActivity : AppCompatActivity() {

    private lateinit var tvShowImageView: ImageView
    private lateinit var tvShowNameTextView: TextView
    private lateinit var tvShowGenreTextView: TextView
    private lateinit var tvShowSummaryTextView: TextView
    private lateinit var addToListButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.show_details_screen)

        tvShowImageView = findViewById(R.id.tvShowImageView)
        tvShowNameTextView = findViewById(R.id.tvShowNameTextView)
        tvShowGenreTextView = findViewById(R.id.tvShowGenreTextView)
        tvShowSummaryTextView = findViewById(R.id.tvShowSummaryTextView)
        addToListButton = findViewById(R.id.addToListButton)

        val showId = intent.getIntExtra("SHOW_ID", -1)
        val showName = intent.getStringExtra("SHOW_NAME")
        val showGenres = intent.getStringExtra("SHOW_GENRES")
        val showSummary = intent.getStringExtra("SHOW_SUMMARY")
        val showImage = intent.getStringExtra("SHOW_IMAGE")

        if (showId != -1) {
            tvShowNameTextView.text = showName
            tvShowGenreTextView.text = showGenres
            tvShowSummaryTextView.text = showSummary

            // Log the image URL
            Log.d("ShowDetailsActivity", "Image URL: $showImage")

            // Check if the image URL is null or empty
            if (showImage.isNullOrEmpty()) {
                Log.e("ShowDetailsActivity", "Image URL is null or empty")
            } else {
                Glide.with(this)
                    .load(showImage)
                    .error(R.drawable.ic_launcher_background) // Log error case
                    .into(tvShowImageView)
            }
        } else {
            Log.e("ShowDetailsActivity", "Invalid Show ID")
        }

        addToListButton.setOnClickListener {
            // Show toast message
            Toast.makeText(this, "Added to list", Toast.LENGTH_SHORT).show()

            // Navigate to Rate and Review screen
            val rateReviewIntent = Intent(this, RateReviewActivity::class.java)
            rateReviewIntent.putExtra("SHOW_ID", showId)
            rateReviewIntent.putExtra("SHOW_NAME", showName)
            rateReviewIntent.putExtra("SHOW_GENRES", showGenres)
            rateReviewIntent.putExtra("SHOW_SUMMARY", showSummary)
            rateReviewIntent.putExtra("SHOW_IMAGE", showImage)
            startActivity(rateReviewIntent)
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
    }
}
