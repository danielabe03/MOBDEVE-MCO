package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import android.widget.Button
import com.mobdeve.s12.abe.daniel.mco3.database.DatabaseHelper

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        // Initialize views
        emailInputLayout = findViewById(R.id.tilEmail)
        passwordInputLayout = findViewById(R.id.tilPassword)
        loginButton = findViewById(R.id.btnLogin)
        signUpButton = findViewById(R.id.btnSignUp)

        // Set up click listeners
        loginButton.setOnClickListener { attemptLogin() }
        signUpButton.setOnClickListener { navigateToSignUp() }
    }

    private fun attemptLogin() {
        val email = emailInputLayout.editText?.text.toString()
        val password = passwordInputLayout.editText?.text.toString()

        // Clear previous errors
        emailInputLayout.error = null
        passwordInputLayout.error = null

        // Validate input
        if (email.isEmpty()) {
            emailInputLayout.error = "Email is required"
            return
        }

        if (password.isEmpty()) {
            passwordInputLayout.error = "Password is required"
            return
        }

        val cursor = dbHelper.getUser(email, password)
        if (cursor != null && cursor.moveToFirst()) {
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            sessionManager.saveUserSession(userId)
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
            navigateToHome()
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHome() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // Finish this activity so the user can't navigate back to it
    }
}
