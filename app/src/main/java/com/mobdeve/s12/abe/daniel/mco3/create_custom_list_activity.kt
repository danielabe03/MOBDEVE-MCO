package com.mobdeve.s12.abe.daniel.mco3

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.s12.abe.daniel.mco3.database.DatabaseHelper

class CreateCustomListActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_custom_list)

        dbHelper = DatabaseHelper(this)

        val etListName: EditText = findViewById(R.id.editTextListName)
        val btnCreateList: Button = findViewById(R.id.btnCreateList)

        btnCreateList.setOnClickListener {
            val listName = etListName.text.toString()

            if (listName.isNotEmpty()) {
                dbHelper.addCustomList(listName)
                val intent = Intent(this, CustomListsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}
