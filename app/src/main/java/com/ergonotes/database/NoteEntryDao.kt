package com.ergonotes.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteEntryDao {

    @Insert
    suspend fun insert(note: NoteEntry)

    @Update
    suspend fun update(note: NoteEntry)

    @Query("SELECT * from note_entries_table WHERE noteId = :key")
    suspend fun get(key: Long): NoteEntry

    @Query("DELETE FROM note_entries_table")
    suspend fun clear()

    @Query("SELECT * FROM note_entries_table ORDER BY noteId DESC")
    fun getAllNotes(): LiveData<List<NoteEntry>>

    @Query("SELECT * FROM note_entries_table ORDER BY noteId DESC LIMIT 1")
    suspend fun getNote(): NoteEntry?

    @Query("SELECT * from note_entries_table WHERE noteId = :key")
    fun getNoteWithId(key: Long): LiveData<NoteEntry>
}
