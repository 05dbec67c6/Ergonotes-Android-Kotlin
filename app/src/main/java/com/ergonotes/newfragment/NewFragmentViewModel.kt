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
    dataSource: NoteEntryDao
) : ViewModel() {

// -------------------------------------------------------------------------------------------------
// General variables----------------------------------------------------------------------------------------

    val database = dataSource

    private val _note = MutableLiveData<NoteEntry?>()
    val note: LiveData<NoteEntry?>
        get() = _note

// -------------------------------------------------------------------------------------------------
// Initialize---------------------------------------------------------------------------------------

    init {
        initializeNoteEntry()
    }

    private fun initializeNoteEntry() {
        viewModelScope.launch {
            _note.value = getNoteFromDatabase()
        }
    }

    private suspend fun getNoteFromDatabase(): NoteEntry? {
        return database.getNote()
    }

// -------------------------------------------------------------------------------------------------
// Updating the database on buttonclick-------------------------------------------------------------

    fun onSetNote(titleString: String, noteString: String) {
        viewModelScope.launch {
            val note = database.get(noteEntryKey)
            note.noteEntryTitle = titleString
            note.noteEntryNote = noteString
            database.update(note)
        }
    }
}





