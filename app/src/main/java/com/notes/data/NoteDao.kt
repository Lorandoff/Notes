package com.notes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAll(): List<NoteDbo>

    @Insert
    fun insertAll(vararg notes: NoteDbo)

    @Delete
    fun DeleteNote(note : NoteDbo)

    @Query("SELECT * FROM notes ORDER BY " +
            " CASE WHEN :isAsc = 1 THEN modifiedAt END ASC," +
            "CASE WHEN :isAsc = 2 THEN modifiedAt END DESC")
    fun sortByModified(isAsc : Int) : List<NoteDbo>

}