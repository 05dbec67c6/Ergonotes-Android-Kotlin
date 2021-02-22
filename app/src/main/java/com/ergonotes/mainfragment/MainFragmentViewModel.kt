package com.ergonotes.mainfragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.launch

class MainFragmentViewModel(dataSource: NoteEntryDao) :
    ViewModel() {

// -------------------------------------------------------------------------------------------------
// General variables----------------------------------------------------------------------------------------

    val database = dataSource

    private var note = MutableLiveData<NoteEntry?>()

    // For UIControllers Submit List
    val notes = database.getAllNotes()

    fun getNote() = note

    init {
        initializeNoteEntry()
    }
    val doper: String = note.value?.noteEntryNote ?: "isnull"
    private fun initializeNoteEntry() {
        viewModelScope.launch {
            note.value = getNoteFromDatabase()
        }
    }

// -------------------------------------------------------------------------------------------------
// Add new entry to database------------------------------------------------------------------------

    fun onAdd() {
        viewModelScope.launch {
            val newNote = NoteEntry()
            insert(newNote)
            note.value = getNoteFromDatabase()
        }
    }

    private suspend fun insert(note: NoteEntry) {
        database.insert(note)
    }

    private suspend fun getNoteFromDatabase(): NoteEntry? {
        return database.getNote()
    }

// -------------------------------------------------------------------------------------------------
// Clear function-----------------------------------------------------------------------------------

    fun onClear() {
        viewModelScope.launch {
            clear()
            note.value = null
        }
    }

    private suspend fun clear() {
        database.clear()
    }


// -------------------------------------------------------------------------------------------------
// Click on NoteEntry-------------------------------------------------------------------------------

    private val _navigateToNewFragment = MutableLiveData<Long>()

    val navigateToNewFragment
        get() = _navigateToNewFragment

    fun onNoteEntryClicked(id: Long) {
        _navigateToNewFragment.value = id
    }

/*    private suspend fun update(note: NoteEntry) {
        database.update(note)
    }*/
}


/*



private var _showSnackbarEvent = MutableLiveData<Boolean?>()
val showSnackBarEvent: LiveData<Boolean?>
    get() = _showSnackbarEvent
private val _navigateToSleepQuality = MutableLiveData<NoteEntry>()
val navigateToSleepQuality: LiveData<NoteEntry>
    get() = _navigateToSleepQuality
fun doneShowingSnackbar() {
    _showSnackbarEvent.value = null
}




private suspend fun getTonightFromDatabase(): NoteEntry? {
    var night = database.getTonight()
    return night
}
private suspend fun insert(night: NoteEntry) {
    database.insert(night)
}
private suspend fun update(night: NoteEntry) {
    database.update(night)
}

fun onStart() {
    viewModelScope.launch {
        val newNight = NoteEntry()

        insert(newNight)

        tonight.value = getTonightFromDatabase()
    }
}


}*/