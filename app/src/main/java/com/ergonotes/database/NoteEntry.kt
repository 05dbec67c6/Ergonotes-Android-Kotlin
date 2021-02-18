package com.ergonotes.database

import android.provider.ContactsContract
import android.view.View
import android.widget.Button
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ergonotes.R

@Entity(tableName = "note_entries_table")
data class NoteEntry(

    @PrimaryKey(autoGenerate = true)
    var noteId: Long = 0L,

    @ColumnInfo(name = "note_entry_title")
    val noteEntryTitle: String = "title_dummy",

    @ColumnInfo(name = "note_entry_note")
    var noteEntryNote: String = "note_dummy",
)