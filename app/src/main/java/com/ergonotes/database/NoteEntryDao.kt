package com.ergonotes.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteEntryDao {

    @Insert
    suspend fun insertNewNote(note: NoteEntry)

    // I can only guess, that the update method is connected to changing
    // the room database in order to sort them by position
    @Update
    suspend fun updateNote(note: NoteEntry)

    @Delete
    suspend fun deleteTargetNote(note: NoteEntry)

    @Query("DELETE FROM note_entries_table")
    suspend fun clearDatabase()

    @Query("SELECT * FROM note_entries_table ORDER BY noteId DESC")
    fun getAllNotes(): LiveData<List<NoteEntry>>

    @Query("SELECT * FROM note_entries_table ORDER BY position DESC")
    fun getAllNotesByPosition(): LiveData<List<NoteEntry>>

    @Query("SELECT * FROM note_entries_table ORDER BY noteId DESC LIMIT 1")
    suspend fun getLatestNoteFromDatabase(): NoteEntry?

    @Query("SELECT * from note_entries_table WHERE noteId = :key")
    suspend fun getTargetNote(key: Long): NoteEntry

    @Query("SELECT * from note_entries_table WHERE position = :position")
    suspend fun getTargetNoteByPosition(position: Int): NoteEntry

    @Query("SELECT * from note_entries_table WHERE noteId = :key")
    fun getNoteWithId(key: Long): LiveData<NoteEntry>


}


