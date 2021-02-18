package com.ergonotes.mainfragment

import android.app.Application
import androidx.lifecycle.*
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragmentViewModel(
    dataSource: NoteEntryDao,
    application: Application) : ViewModel() {

    // Used for submit whole list in recyclerview
    val database = dataSource
    val notes = database.getAllNotes()

    fun onNoteEntryClicked(id: Long) {
        _navigateToNewFragment.value = id
    }
    private val _navigateToNewFragment = MutableLiveData<Long>()
    val navigateToNewFragment
        get() = _navigateToNewFragment
}
    /*

    private var tonight = MutableLiveData<NoteEntry?>()

    private var _showSnackbarEvent = MutableLiveData<Boolean?>()
    val showSnackBarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent
    private val _navigateToSleepQuality = MutableLiveData<NoteEntry>()
    val navigateToSleepQuality: LiveData<NoteEntry>
        get() = _navigateToSleepQuality
    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }
    init {
        initializeTonight()
    }


    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
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
    private suspend fun clear() {
        database.clear()
    }
    fun onStart() {
        viewModelScope.launch {
            val newNight = NoteEntry()

            insert(newNight)

            tonight.value = getTonightFromDatabase()
        }
    }

    fun onClear() {
        viewModelScope.launch {
            clear()
            tonight.value = null
            _showSnackbarEvent.value = true
        }
    }
}*/