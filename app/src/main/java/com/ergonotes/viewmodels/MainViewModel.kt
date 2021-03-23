package com.ergonotes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(
    private val dataSource: NoteEntryDao
) :
    ViewModel() {

    val allNotes = dataSource.getAllNotes()

    private val _navigateToNewNote = MutableLiveData<NoteEntry>()
    val navigateToNewNote: LiveData<NoteEntry>
        get() = _navigateToNewNote

// -------------------------------------------------------------------------------------------------
// Initializing the list----------------------------------------------------------------------------

    private suspend fun getLatestNoteFromDatabase(): NoteEntry? {
        return dataSource.getLatestNoteFromDatabase()
    }

    private var thisNote = MutableLiveData<NoteEntry?>()

    private fun initializeNote() {
        viewModelScope.launch {
            thisNote.value = getLatestNoteFromDatabase()
        }
    }

    init {
        initializeNote()
    }

// -------------------------------------------------------------------------------------------------
// Create new note and navigate to it---------------------------------------------------------------

    private val _navigateToNewFragment = MutableLiveData<NoteEntry>()
    val navigateToNewFragment: LiveData<NoteEntry>
        get() = _navigateToNewFragment

    private suspend fun insertNewNote(note: NoteEntry) {
        withContext(Dispatchers.IO) {
            dataSource.insertNewNote(note)
        }
    }

    fun onAddNote(
        noteEntryBackgroundColor: Int,
        noteEntryTextColor: Int,
        noteEntryNoteSize: Float,
        noteEntryTitleSize: Float
    ) {
        viewModelScope.launch {

            val newNote = NoteEntry(
                noteEntryTitle = "",
                noteEntryNote = "",
                noteEntryBackgroundColor = noteEntryBackgroundColor.toString().toInt(),
                noteEntryTextColor = noteEntryTextColor.toString().toInt(),
                noteEntryNoteTextSize = noteEntryNoteSize,
                noteEntryTitleTextSize = noteEntryTitleSize
            )

            insertNewNote(newNote)

            _navigateToNewFragment.value = getLatestNoteFromDatabase()

        }
    }
}

