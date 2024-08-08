package com.mobdeve.s12.abe.daniel.mco3

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.abe.daniel.mco3.database.DatabaseHelper

class CreateCustomListActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_custom_list)

        dbHelper = DatabaseHelper(this)
        sessionManager = SessionManager(this)

        val editTextListName: EditText = findViewById(R.id.editTextListName)
        val btnCreateList: Button = findViewById(R.id.btnCreateList)

        btnCreateList.setOnClickListener {
            val listName = editTextListName.text.toString()
            if (listName.isNotEmpty()) {
                val userId = sessionManager.getUserSession()
                dbHelper.addCustomList(userId, listName)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
}
