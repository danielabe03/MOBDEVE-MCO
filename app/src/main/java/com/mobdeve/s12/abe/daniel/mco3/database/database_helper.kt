package com.mobdeve.s12.abe.daniel.mco3.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2  // Incremented the version
        private const val DATABASE_NAME = "UserDatabase.db"
        private const val TABLE_USERS = "Users"
        private const val TABLE_REVIEWS = "Reviews"

        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"

        private const val COLUMN_USER_ID = "user_id"
        private const val COLUMN_SHOW_ID = "show_id"
        private const val COLUMN_SHOW_NAME = "show_name"
        private const val COLUMN_GENRES = "genres"
        private const val COLUMN_SUMMARY = "summary"
        private const val COLUMN_IMAGE_URL = "image_url"
        private const val COLUMN_RATING = "rating"
        private const val COLUMN_COMMENT = "comment"
        private const val COLUMN_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_PASSWORD + " TEXT" + ")")
        db.execSQL(createUserTable)

        val createReviewTable = ("CREATE TABLE " + TABLE_REVIEWS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_ID + " INTEGER,"
                + COLUMN_SHOW_ID + " INTEGER,"
                + COLUMN_SHOW_NAME + " TEXT,"
                + COLUMN_GENRES + " TEXT,"
                + COLUMN_SUMMARY + " TEXT,"
                + COLUMN_IMAGE_URL + " TEXT,"
                + COLUMN_RATING + " REAL,"
                + COLUMN_COMMENT + " TEXT,"
                + COLUMN_STATUS + " TEXT" + ")")
        db.execSQL(createReviewTable)

        Log.d("DatabaseHelper", "Database tables created.")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_REVIEWS")
            onCreate(db)
            Log.d("DatabaseHelper", "Database upgraded from version $oldVersion to $newVersion.")
        }
    }

    fun addUser(email: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, password)

        val result = db.insert(TABLE_USERS, null, values)
        db.close()
        return result
    }

    fun getUser(email: String, password: String): Cursor? {
        val db = this.readableDatabase
        return db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID, COLUMN_EMAIL, COLUMN_PASSWORD),
            "$COLUMN_EMAIL=? AND $COLUMN_PASSWORD=?",
            arrayOf(email, password),
            null, null, null, null
        )
    }

    fun isUserExists(email: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_ID),
            "$COLUMN_EMAIL=?",
            arrayOf(email),
            null, null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun addReview(userId: Int, showId: Int, showName: String?, genres: String?, summary: String?, imageUrl: String?, rating: Float, comment: String?, status: String?): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId)
            put(COLUMN_SHOW_ID, showId)
            put(COLUMN_SHOW_NAME, showName)
            put(COLUMN_GENRES, genres)
            put(COLUMN_SUMMARY, summary)
            put(COLUMN_IMAGE_URL, imageUrl)
            put(COLUMN_RATING, rating)
            put(COLUMN_COMMENT, comment)
            put(COLUMN_STATUS, status)
        }

        val result = db.insert(TABLE_REVIEWS, null, values)
        db.close()
        return result
    }

    fun getReviews(userId: Int): List<ContentValues> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_REVIEWS,
            null,
            "$COLUMN_USER_ID=?",
            arrayOf(userId.toString()),
            null, null, null
        )

        val reviews = mutableListOf<ContentValues>()
        while (cursor.moveToNext()) {
            val values = ContentValues()
            values.put(COLUMN_SHOW_ID, cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SHOW_ID)))
            values.put(COLUMN_SHOW_NAME, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOW_NAME)))
            values.put(COLUMN_GENRES, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GENRES)))
            values.put(COLUMN_SUMMARY, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SUMMARY)))
            values.put(COLUMN_IMAGE_URL, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URL)))
            values.put(COLUMN_RATING, cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)))
            values.put(COLUMN_COMMENT, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENT)))
            values.put(COLUMN_STATUS, cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)))
            reviews.add(values)
        }
        cursor.close()
        return reviews
    }
}
