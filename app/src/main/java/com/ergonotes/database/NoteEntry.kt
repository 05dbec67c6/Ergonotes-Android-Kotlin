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
    var noteEntryTitle: String = "",

    @ColumnInfo(name = "note_entry_note")
    var noteEntryNote: String = "",

    @ColumnInfo(name = "note_entry_background_color")
    var noteEntryBackgroundColor: Int = Color.parseColor("#FF00FF"),

    @ColumnInfo(name = "note_entry_font_color")
    var noteEntryTextColor: Int = Color.parseColor("#FFFFFF")
)