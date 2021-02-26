package com.ergonotes.mainfragment

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ergonotes.database.NoteEntry

@BindingAdapter("note_entry")
fun TextView.setNoteNoteString(item: NoteEntry?) {
    item?.let {
        text = item.noteEntryNote
    }
}