package com.ergonotes.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteEntryDao {

    @Insert
    fun addNote(note: NoteEntry)

    @Update
    fun updateNote(note: NoteEntry)

    @Delete
    fun deleteNote(note: NoteEntry)

    @Query("SELECT * FROM note_entries_table ORDER BY noteId DESC")
    fun getAllNotes(): LiveData<List<NoteEntry>>

    @Query("SELECT * FROM note_entries_table ORDER BY noteId DESC LIMIT 1")
    suspend fun getNotes(): NoteEntry?

    @Query("SELECT * from note_entries_table WHERE noteId = :key")
    fun getNoteWithId(key: Long): LiveData<NoteEntry>
}
