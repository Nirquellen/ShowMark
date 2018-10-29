package com.example.dragonmaster.showmark

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHandler(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "showDB.db"
        val TABLE_SHOWS = "shows"

        val COLUMN_ID = "_id"
        val COLUMN_SHOWNAME = "show_name"
        val COLUMN_CURRENTEPISODE = "current_episode"
        val COLUMN_ALLEPISODES = "all_episodes"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_SHOWS_TABLE = ("CREATE TABLE " + TABLE_SHOWS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_SHOWNAME + " TEXT," + COLUMN_CURRENTEPISODE
                + " INTEGER," + COLUMN_ALLEPISODES + " INTEGER" + ")")
        db?.execSQL(CREATE_SHOWS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SHOWS")
        onCreate(db)
    }

    fun addShow(show: ShowItem) : Boolean {
        val values = ContentValues()
        values.put(COLUMN_SHOWNAME, show.showName)
        values.put(COLUMN_CURRENTEPISODE, show.currentEpisode)
        values.put(COLUMN_ALLEPISODES, show.allEpisodes)

        val db = this.writableDatabase

        val success = db.insert(TABLE_SHOWS, null, values)
        db.close()
        return (Integer.parseInt("$success") != -1)
    }

    fun findShow(showName: String): ShowItem? {
        val query = "SELECT * FROM $TABLE_SHOWS WHERE $COLUMN_SHOWNAME =  \"$showName\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)
        var show: ShowItem? = null

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()

            val id = Integer.parseInt(cursor.getString(0))
            val name = cursor.getString(1)
            val currentEpisode = Integer.parseInt(cursor.getString(2))
            val allEpisodes = Integer.parseInt(cursor.getString(2))
            show = ShowItem(id, name, currentEpisode, allEpisodes)
            cursor.close()
        }
        db.close()
        return show
    }

    fun updateShow(show: ShowItem) : Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_SHOWNAME, show.showName)
        values.put(COLUMN_CURRENTEPISODE, show.currentEpisode)
        values.put(COLUMN_ALLEPISODES, show.allEpisodes)
        val success = db.update(TABLE_SHOWS, values, COLUMN_ID + "=?", arrayOf(show.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun deleteShow(showName: String): Boolean {
        var result = false
        val query = "SELECT * FROM $TABLE_SHOWS WHERE $COLUMN_SHOWNAME = \"$showName\""
        val db = this.writableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val id = Integer.parseInt(cursor.getString(0))
            db.delete(TABLE_SHOWS, COLUMN_ID + " = ?", arrayOf(id.toString()))
            cursor.close()
            result = true
        }
        db.close()
        return result
    }

    fun allShows(): MutableList<ShowItem> {
        val showList = mutableListOf<ShowItem>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_SHOWS"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val show = ShowItem(
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SHOWNAME)),
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_CURRENTEPISODE))),
                            Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ALLEPISODES))))
                    showList.add(show)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return showList
    }
}