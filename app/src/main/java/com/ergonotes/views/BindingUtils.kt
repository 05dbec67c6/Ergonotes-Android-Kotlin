package com.ergonotes.views

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ergonotes.database.NoteEntry

@BindingAdapter("note")
fun TextView.setNote(item: NoteEntry?) {
    item?.let {
        text = item.noteEntryTitle
        text = item.noteEntryNote
        setBackgroundColor(item.noteEntryBackgroundColor)
    }
}
