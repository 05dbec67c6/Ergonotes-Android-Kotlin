package com.ergonotes.newfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.launch

class NewFragmentViewModel(
    private val noteEntryKey: Long = 0L,
    dataSource: NoteEntryDao
) : ViewModel() {

    val database = dataSource

    private val note: LiveData<NoteEntry> = database.getNoteWithId(noteEntryKey)

    fun getNote() = note

    fun onSetNote(titleString: String, noteString: String) {
        viewModelScope.launch {
            val note = database.get(noteEntryKey)
            note.noteEntryTitle = titleString
            note.noteEntryNote = noteString
            database.update(note)
        }
    }
}