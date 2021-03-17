package com.ergonotes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.launch

class DialogDeleteViewModel(
    private val noteEntryKey: Long = 0L,
    private val dataSource: NoteEntryDao
) : ViewModel() {

// -------------------------------------------------------------------------------------------------
// Set and initialize-------------------------------------------------------------------------------

    private val note: LiveData<NoteEntry> = dataSource.getNoteWithId(noteEntryKey)
    fun getNote() = note

// -------------------------------------------------------------------------------------------------
// Button apply update database --------------------------------------------------------------------

    fun onSetNote(titleString: String, noteString: String) {
        viewModelScope.launch {
            val note = dataSource.getTargetNote(noteEntryKey)
            note.noteEntryTitle = titleString
            note.noteEntryNote = noteString
            dataSource.updateNote(note)
        }
    }

    // -------------------------------------------------------------------------------------------------
// Button update database --------------------------------------------------------------------------
    private suspend fun deleteTargetNote(note: NoteEntry) {
        dataSource.deleteTargetNote(note)
    }

    fun deleteTargetNote() {
        viewModelScope.launch {
            val note = dataSource.getTargetNote(noteEntryKey)
            deleteTargetNote(note)
        }
    }
}


