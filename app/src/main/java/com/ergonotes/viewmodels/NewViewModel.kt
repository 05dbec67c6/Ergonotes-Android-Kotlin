package com.ergonotes.viewmodels

import android.app.Application
import androidx.lifecycle.*
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

    private var newestNote = MutableLiveData<NoteEntry?>()
    private suspend fun insertNewNote(note: NoteEntry) {
        dataSource.insertNewNote(note)
    }

    private suspend fun getLatestNoteFromDatabase(): NoteEntry? {
        return dataSource.getLatestNoteFromDatabase()
    }

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    private val note2: LiveData<NoteEntry> = dataSource.getNoteWithId(noteEntryKey)
    fun getNote2() = note2
    // -------------------------------------------------------------------------------------------------
// Button apply update database --------------------------------------------------------------------
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun onSetNote(titleString: String, noteString: String) {
        viewModelScope.launch {
            val note = dataSource.getTargetNote(noteEntryKey)
            note.noteEntryTitle = titleString
            note.noteEntryNote = noteString
            dataSource.updateNote(note)
        }
    }

    fun onSetSleepQuality(note: String) {
        viewModelScope.launch {
            // IO is a thread pool for running operations that access the disk, such as
            // our Room database.
            val tonight = dataSource.getTargetNote(noteEntryKey) ?: return@launch
            tonight.noteEntryNote = note
            dataSource.updateNote(tonight)

            // Setting this state variable to true will alert the observer and trigger navigation.
            _navigateToSleepTracker.value = true
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


