package com.ergonotes.mainfragment

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ergonotes.database.NoteEntry

class BindingUtils {

    @BindingAdapter("note_TitleString")
    fun TextView.setNoteTitleString(item: NoteEntry?) {
        item?.let {
            text = item.noteEntryTitle
        }
    }

    @BindingAdapter("note_NoteString")
    fun TextView.setNoteNoteString(item: NoteEntry?) {
        item?.let {
            text = item.noteEntryNote
        }
    }
}