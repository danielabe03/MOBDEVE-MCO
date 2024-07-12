package com.mobdeve.s12.abe.daniel.mco3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import android.widget.Button
import android.widget.TextView
import android.widget.ImageButton

class SignUpActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var confirmPasswordInputLayout: TextInputLayout
    private lateinit var signUpButton: Button
    private lateinit var loginTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_screen)

        // Initialize views
        emailInputLayout = findViewById(R.id.tilEmail)
        passwordInputLayout = findViewById(R.id.tilPassword)
        confirmPasswordInputLayout = findViewById(R.id.tilConfirmPassword)
        signUpButton = findViewById(R.id.btnSignUp)
        loginTextView = findViewById(R.id.tvLogin)

        // Set up click listeners
        backButton.setOnClickListener { onBackPressed() }
        signUpButton.setOnClickListener { attemptSignUp() }
        loginTextView.setOnClickListener { navigateToLogin() }
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

        // TODO: Implement actual sign up logic here
        // For now, we'll just show a toast message
        Toast.makeText(this, "Sign up attempt with: $email", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToLogin() {
        // TODO: Implement navigation to login screen
        Toast.makeText(this, "Navigate to Login", Toast.LENGTH_SHORT).show()
        // You might want to finish this activity if you're navigating back to login
        // finish()
    }
}