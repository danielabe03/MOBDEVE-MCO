package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import android.widget.Button
import com.mobdeve.s12.abe.daniel.mco3.database.DatabaseHelper

class SignUpActivity : AppCompatActivity() {

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var signUpButton: Button
    private lateinit var loginButton: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_screen)

        dbHelper = DatabaseHelper(this)

        // Initialize views
        emailInputLayout = findViewById(R.id.tilEmail)
        passwordInputLayout = findViewById(R.id.tilPassword)
        confirmPasswordInputLayout = findViewById(R.id.tilConfirmPassword)
        signUpButton = findViewById(R.id.btnSignUp)
        loginButton = findViewById(R.id.btnLogin)

        // Set up click listeners
        signUpButton.setOnClickListener { attemptSignUp() }
        loginButton.setOnClickListener { navigateToLogin() }
    }

    private fun attemptSignUp() {
        val email = emailInputLayout.editText?.text.toString()
        val password = passwordInputLayout.editText?.text.toString()
        val confirmPassword = confirmPasswordInputLayout.editText?.text.toString()

        // Clear previous errors
        emailInputLayout.error = null
        passwordInputLayout.error = null
        confirmPasswordInputLayout.error = null

        // Validate input
        if (email.isEmpty()) {
            emailInputLayout.error = "Email is required"
            return
        }

        if (password.isEmpty()) {
            passwordInputLayout.error = "Password is required"
            return
        }

        if (confirmPassword.isEmpty()) {
            confirmPasswordInputLayout.error = "Please confirm your password"
            return
        }

        if (password != confirmPassword) {
            confirmPasswordInputLayout.error = "Passwords do not match"
            return
        }

        if (dbHelper.isUserExists(email)) {
            emailInputLayout.error = "Email already exists"
            return
        }

        val result = dbHelper.addUser(email, password)
        if (result > 0) {
            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
            navigateToLogin()
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Finish this activity so the user can't navigate back to it
    }
}
