package com.ergonotes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.launch

class NewViewModel(
    private val noteEntryKey: Long = 0L,
    private val dataSource: NoteEntryDao,
    application: Application
) : AndroidViewModel(application) {

// -------------------------------------------------------------------------------------------------
// Set and initialize-------------------------------------------------------------------------------

    private val note: LiveData<NoteEntry> = dataSource.getNoteWithId(noteEntryKey)

    fun getNote() = note

    fun onSetNote(titleString: String, noteString: String) {
        viewModelScope.launch {
            val note = dataSource.getTargetNote(noteEntryKey)
            note.noteEntryTitle = titleString
            note.noteEntryNote = noteString
            dataSource.updateNote(note)
        }
    }


}