package com.ergonotes.database

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_entries_table")
data class NoteEntry(

    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0L,

    @ColumnInfo(name = "note_entry_title")
    var noteEntryTitle: String = "dummy_title",

    @ColumnInfo(name = "note_entry_note")
    var noteEntryNote: String = "dummy_note",

    @ColumnInfo(name = "note_entry_background_color")
    var noteEntryBackgroundColor: Int = Color.parseColor("#FFFFFF"),

    @ColumnInfo(name = "note_entry_text_color")
    var noteEntryTextColor: Int = Color.parseColor("#000000"),

    @ColumnInfo(name = "note_entry_note_text_size")
    var noteEntryNoteTextSize: Float = 45F,

    @ColumnInfo(name = "note_entry_title_text_size")
    var noteEntryTitleTextSize: Float = 45F
)