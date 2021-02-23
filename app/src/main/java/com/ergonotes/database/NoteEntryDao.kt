package com.ergonotes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface NoteEntryDao {

    @Insert
    suspend fun insertNewNote(note: NoteEntry)

    @Update
    suspend fun updateNote(note: NoteEntry)

    @Query("DELETE FROM note_entries_table")
    suspend fun clearDatabase()

    @Query("SELECT * FROM note_entries_table ORDER BY noteId DESC")
    fun getAllNotes(): LiveData<List<NoteEntry>>

    @Query("SELECT * FROM note_entries_table ORDER BY noteId DESC LIMIT 1")
    suspend fun getLatestNoteFromDatabase(): NoteEntry?

    @Query("SELECT * from note_entries_table WHERE noteId = :key")
    suspend fun getTargetNote(key: Long): NoteEntry

    @Query("SELECT * from note_entries_table WHERE noteId = :key")
    fun getNoteWithId(key: Long): LiveData<NoteEntry>
}


