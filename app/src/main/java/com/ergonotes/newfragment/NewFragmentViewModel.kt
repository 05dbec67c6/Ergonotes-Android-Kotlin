package com.ergonotes.newfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.launch

class NewFragmentViewModel(
    private val noteEntryKey: Long = 0L,
    private val dataSource: NoteEntryDao
) : ViewModel() {

// -------------------------------------------------------------------------------------------------
// Set and initialize-------------------------------------------------------------------------------

    private val note: LiveData<NoteEntry> = dataSource.getNoteWithId(noteEntryKey)
    fun getNote() = note

    private var newestNote = MutableLiveData<NoteEntry?>()
    private suspend fun insertNewNote(note: NoteEntry) {
        dataSource.insertNewNote(note)
    }
    private suspend fun getLatestNoteFromDatabase(): NoteEntry? {
        return dataSource.getLatestNoteFromDatabase()
    }
    fun onPressNewNote() {
        viewModelScope.launch {

            // Variable of databases entity
            val newNote = NoteEntry()

            insertNewNote(newNote)

            // Set the values of the NoteEntry entity
            newestNote.value = getLatestNoteFromDatabase()
        }
    }

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

    private val _navigateToNewFragment = MutableLiveData<Long>()
    val navigateToNewFragment
        get() = _navigateToNewFragment

    fun onNoteClicked(id: Long) {
        _navigateToNewFragment.value = id
    }
}


