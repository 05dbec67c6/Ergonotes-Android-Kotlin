package com.ergonotes.viewmodels

import android.graphics.Color
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

    // Variable of the noteKeys in DAO for navigating
    var newestNote = MutableLiveData<NoteEntry?>()

    val allNotes2 = dataSource.getAllNotes()

    private val _navigateToNewFragment = MutableLiveData<NoteEntry>()


    val navigateToNewFragment: LiveData<NoteEntry>
        get() = _navigateToNewFragment
    private val _navigateToSleepDataQuality = MutableLiveData<Long>()
    private val _navigateToNewFragmentNew = MutableLiveData<Long>()
    val navigateToSleepDataQuality
        get() = _navigateToSleepDataQuality
    val navigateToNewFragmentNew
        get() = _navigateToNewFragmentNew

    fun onSleepNightClicked(id: Long) {
        _navigateToSleepDataQuality.value = id
    }


// -------------------------------------------------------------------------------------------------
// General Variables--------------------------------------------------------------------------------

    // Set notes as liveData


// -------------------------------------------------------------------------------------------------
// Button add note----------------------------------------------------------------------------------

    // DAO-Function - Insert a new entry in the database
    private suspend fun insertNewNote(note: NoteEntry) {
        dataSource.insertNewNote(note)
    }

    private val _navigateToSleepQuality = MutableLiveData<NoteEntry>()

    val navigateToSleepQuality: LiveData<NoteEntry>
        get() = _navigateToSleepQuality

    private var thisNote = MutableLiveData<NoteEntry?>()

    private fun initializeTonight() {
        viewModelScope.launch {
            thisNote.value = getLatestNoteFromDatabase()
        }
    }
    // Create new note
    fun onAddNote(
        noteEntryBackgroundColor: Int,
        noteEntryTextColor: Int,
        noteEntryNoteSize: Float,
        noteEntryTitleSize: Float
    ) {
        viewModelScope.launch {
            // Create a new night, which captures the current time,
            // and insert it into the database.
            val newNight = NoteEntry(
                noteEntryTitle = "",
                noteEntryNote = "",
                noteEntryBackgroundColor = noteEntryBackgroundColor.toString().toInt(),
                noteEntryTextColor = noteEntryTextColor.toString().toInt(),
                noteEntryNoteTextSize = noteEntryNoteSize,
                noteEntryTitleTextSize = noteEntryTitleSize
            )

            insertNewNote(newNight)

            newestNote.value = getLatestNoteFromDatabase()

            val oldNight = newestNote.value
//
//            update(oldNight)

            _navigateToSleepQuality.value = oldNight
        }
    }
    private suspend fun update(night: NoteEntry) {
        withContext(Dispatchers.IO) {
            dataSource.updateNote(night)
        }
    }

    fun onClear() {
        viewModelScope.launch {
            // Clear the database table.
            clear()
        }
    }

    fun onNewNoteClicke2d(id: Long) {
        _navigateToNewFragmentNew.value = id
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            dataSource.clearDatabase()
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

    private fun initializeNotes() {
        viewModelScope.launch {
            newestNote.value = getLatestNoteFromDatabase()
        }
    }

    init {
        //initializeNotes()
        initializeTonight()
    }

    fun changeDefaultBackgroundColor(){

    }

}

