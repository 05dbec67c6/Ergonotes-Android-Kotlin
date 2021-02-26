package com.ergonotes.mainfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.launch

class MainFragmentViewModel(
    private val dataSource: NoteEntryDao
) :
    ViewModel() {


    // Variable of the noteKeys in DAO for navigating
    private val _navigateToNewFragment = MutableLiveData<Long>()
    val navigateToNewFragment
        get() = _navigateToNewFragment

    // Get target note
    fun onNoteClicked(id: Long) {
        _navigateToNewFragment.value = id
    }


// -------------------------------------------------------------------------------------------------
// General Variables--------------------------------------------------------------------------------

    // Set notes as liveData
    private val notes = MutableLiveData<NoteEntry?>()

    // Number of columns of recyclerview
    val numberOfColumns: Int = 3

// -------------------------------------------------------------------------------------------------
// Button add note----------------------------------------------------------------------------------

    // DAO-Function - Insert a new entry in the database
    private suspend fun insertNewNote(note: NoteEntry) {
        dataSource.insertNewNote(note)
    }

    // Create new note
    fun onPressNewNote() {
        viewModelScope.launch {

            // Variable of databases entity
            val newNote = NoteEntry()

            insertNewNote(newNote)

            // Set the values of the NoteEntry entity
            notes.value = getLatestNoteFromDatabase()
        }
    }

// -------------------------------------------------------------------------------------------------
// RecyclerView - Submitting the whole list of notes -----------------------------------------------

    // DAO - Variable - Get a list of all the notes
    val allNotes = dataSource.getAllNotes()

// -------------------------------------------------------------------------------------------------
// Initializing the list----------------------------------------------------------------------------

    // Get latest note from database in DAO
    private suspend fun getLatestNoteFromDatabase(): NoteEntry? {
        return dataSource.getLatestNoteFromDatabase()
    }

    init {
        initializeNotes()
    }

    private fun initializeNotes() {
        viewModelScope.launch {
            notes.value = getLatestNoteFromDatabase()
        }
    }

// -------------------------------------------------------------------------------------------------
// LiveDataKeys for Navigating----------------------------------------------------------------------


}

