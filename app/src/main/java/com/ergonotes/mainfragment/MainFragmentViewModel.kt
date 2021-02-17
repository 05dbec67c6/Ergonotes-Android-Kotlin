package com.ergonotes.mainfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragmentViewModel(
    val database: NoteEntryDao,
    application: Application
) : AndroidViewModel(application) {

    private var noteEntries = MutableLiveData<NoteEntry?>()

    val notes = database.getAllNotes()

    init {
        initializeNoteEntries()
    }

    private fun initializeNoteEntries() {
        viewModelScope.launch {
            noteEntries.value = getNoteEntryFromDatabase()
        }
    }

    private suspend fun getNoteEntryFromDatabase(): NoteEntry? {
        return database.getNotes()
    }

    private suspend fun insert(note: NoteEntry) {
        withContext(Dispatchers.IO) {
            database.addNote(note)
        }
    }

    private suspend fun update(note: NoteEntry) {
        withContext(Dispatchers.IO) {
            database.updateNote(note)
        }
    }

    private suspend fun delete(note: NoteEntry) {
        withContext(Dispatchers.IO) {
            database.deleteNote(note)
        }
    }
}



