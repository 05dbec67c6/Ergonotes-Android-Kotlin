package com.ergonotes.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_entries_table")
data class NoteEntry(

    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0L,

    @ColumnInfo(name = "note_entry_title")
    var noteEntryTitle: String = "title_dummy",

    @ColumnInfo(name = "note_entry_note")
    var noteEntryNote: String = "note_dummy",
)