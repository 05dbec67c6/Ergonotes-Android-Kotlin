package com.ergonotes.mainfragment

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.ergonotes.database.NoteEntry

@BindingAdapter("title_entry")
fun TextView.setNoteTitleString(item: NoteEntry?) {
    item?.let {
        text = item.noteEntryTitle
    }
}