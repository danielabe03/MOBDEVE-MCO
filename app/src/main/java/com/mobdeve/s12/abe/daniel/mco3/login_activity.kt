package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import android.widget.Button

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_screen)

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

        // TODO: Implement actual login logic here
        // For now, we'll just show a toast message
        Toast.makeText(this, "Login attempt with: $email", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}