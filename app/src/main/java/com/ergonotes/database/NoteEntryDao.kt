package com.ergonotes.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

@Dao
interface NoteEntryDao {

    @Insert
    fun addNote(note: NoteEntry)

    @Update
    fun updateNote(note: NoteEntry)

    @Delete
    fun deleteNote(note: NoteEntry)
}
