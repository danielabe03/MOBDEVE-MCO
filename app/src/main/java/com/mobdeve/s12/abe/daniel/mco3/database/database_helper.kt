package com.mobdeve.s12.abe.daniel.mco3.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mobdeve.s12.abe.daniel.mco3.models.CustomList
import com.mobdeve.s12.abe.daniel.mco3.models.Show

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "UserDatabase.db"

        private const val TABLE_USERS = "Users"
        private const val TABLE_REVIEWS = "Reviews"
        private const val TABLE_CUSTOM_LISTS = "CustomLists"
        private const val TABLE_SHOWS = "Shows"

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
        private const val COLUMN_CUSTOM_LIST_ID = "custom_list_id"
        private const val COLUMN_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_EMAIL TEXT,"
                + "$COLUMN_PASSWORD TEXT)")

        val createReviewTable = ("CREATE TABLE $TABLE_REVIEWS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_USER_ID INTEGER,"
                + "$COLUMN_SHOW_ID INTEGER,"
                + "$COLUMN_SHOW_NAME TEXT,"
                + "$COLUMN_GENRES TEXT,"
                + "$COLUMN_SUMMARY TEXT,"
                + "$COLUMN_IMAGE_URL TEXT,"
                + "$COLUMN_RATING REAL,"
                + "$COLUMN_COMMENT TEXT,"
                + "$COLUMN_STATUS TEXT)")

        val createCustomListsTable = ("CREATE TABLE $TABLE_CUSTOM_LISTS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_USER_ID INTEGER,"
                + "$COLUMN_NAME TEXT)")

        val createShowsTable = ("CREATE TABLE $TABLE_SHOWS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_SHOW_NAME TEXT,"
                + "$COLUMN_STATUS TEXT,"
                + "$COLUMN_RATING REAL,"
                + "$COLUMN_COMMENT TEXT,"
                + "$COLUMN_CUSTOM_LIST_ID INTEGER,"
                + "FOREIGN KEY($COLUMN_CUSTOM_LIST_ID) REFERENCES $TABLE_CUSTOM_LISTS($COLUMN_ID))")

        db.execSQL(createUserTable)
        db.execSQL(createReviewTable)
        db.execSQL(createCustomListsTable)
        db.execSQL(createShowsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_CUSTOM_LISTS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_USER_ID INTEGER, $COLUMN_NAME TEXT)")
            db.execSQL("CREATE TABLE IF NOT EXISTS $TABLE_SHOWS ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_SHOW_NAME TEXT, $COLUMN_STATUS TEXT, $COLUMN_RATING REAL, $COLUMN_COMMENT TEXT, $COLUMN_CUSTOM_LIST_ID INTEGER, FOREIGN KEY($COLUMN_CUSTOM_LIST_ID) REFERENCES $TABLE_CUSTOM_LISTS($COLUMN_ID))")
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

    fun deleteShow(userId: Int, showId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_REVIEWS, "$COLUMN_USER_ID=? AND $COLUMN_SHOW_ID=?", arrayOf(userId.toString(), showId.toString()))
        db.close()
        return result
    }

    // Custom Lists methods
    fun addCustomList(userId: Int, name: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_USER_ID, userId)
        }
        val result = db.insert(TABLE_CUSTOM_LISTS, null, values)
        db.close()
        return result
    }

    fun getCustomLists(userId: Int): List<CustomList> {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CUSTOM_LISTS,
            null,
            "$COLUMN_USER_ID=?",
            arrayOf(userId.toString()),
            null, null, null
        )
        val customLists = mutableListOf<CustomList>()
        while (cursor.moveToNext()) {
            val customList = CustomList(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            )
            customLists.add(customList)
        }
        cursor.close()
        return customLists
    }

    fun getShowsInCustomList(customListId: Int): List<Show> {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_SHOWS, null, "$COLUMN_CUSTOM_LIST_ID=?", arrayOf(customListId.toString()), null, null, null)
        val shows = mutableListOf<Show>()
        while (cursor.moveToNext()) {
            val show = Show(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SHOW_NAME)) ?: "",
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS)) ?: "",
                cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_RATING)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENT))
            )
            shows.add(show)
        }
        cursor.close()
        return shows
    }

    fun deleteShowFromCustomList(customListId: Int, showId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_SHOWS, "$COLUMN_ID=? AND $COLUMN_CUSTOM_LIST_ID=?", arrayOf(showId.toString(), customListId.toString()))
        db.close()
        return result
    }

    fun addShowToCustomList(customListId: Int, showName: String, rating: Float): Long {
        val db = this.writableDatabase

        // Check if the show already exists in the custom list
        val cursor = db.query(
            TABLE_SHOWS,
            null,
            "$COLUMN_CUSTOM_LIST_ID=? AND $COLUMN_SHOW_NAME=?",
            arrayOf(customListId.toString(), showName),
            null, null, null
        )

        return if (cursor.moveToFirst()) {
            // If the show already exists, update its rating and other information
            val values = ContentValues().apply {
                put(COLUMN_RATING, rating)
            }
            val result = db.update(
                TABLE_SHOWS,
                values,
                "$COLUMN_CUSTOM_LIST_ID=? AND $COLUMN_SHOW_NAME=?",
                arrayOf(customListId.toString(), showName)
            )
            cursor.close()
            result.toLong()
        } else {
            // If the show does not exist, check if it exists in reviews to sync the information
            val reviewCursor = db.query(
                TABLE_REVIEWS,
                null,
                "$COLUMN_SHOW_NAME=?",
                arrayOf(showName),
                null, null, null
            )

            val values = ContentValues().apply {
                put(COLUMN_SHOW_NAME, showName)
                put(COLUMN_RATING, rating)
                put(COLUMN_CUSTOM_LIST_ID, customListId)
                if (reviewCursor.moveToFirst()) {
                    put(COLUMN_STATUS, reviewCursor.getString(reviewCursor.getColumnIndexOrThrow(COLUMN_STATUS)))
                    put(COLUMN_COMMENT, reviewCursor.getString(reviewCursor.getColumnIndexOrThrow(COLUMN_COMMENT)))
                    put(COLUMN_RATING, reviewCursor.getFloat(reviewCursor.getColumnIndexOrThrow(COLUMN_RATING))) // Sync rating
                }
            }
            reviewCursor.close()
            cursor.close()
            val result = db.insert(TABLE_SHOWS, null, values)
            db.close()
            result
        }
    }

    fun deleteCustomList(customListId: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_CUSTOM_LISTS, "$COLUMN_ID=?", arrayOf(customListId.toString()))
        db.delete(TABLE_SHOWS, "$COLUMN_CUSTOM_LIST_ID=?", arrayOf(customListId.toString()))  // Delete associated shows
        db.close()
        return result
    }
}
