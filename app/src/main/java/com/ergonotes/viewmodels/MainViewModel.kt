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
import timber.log.Timber


class MainViewModel(
    private val dataSource: NoteEntryDao
) :
    ViewModel() {


    val allNotes = dataSource.getAllNotes()
    val allNotesByPosition = dataSource.getAllNotesByPosition()

    private val _navigateToNewFragment = MutableLiveData<NoteEntry>()
    val navigateToNewFragment: LiveData<NoteEntry>
        get() = _navigateToNewFragment

// -------------------------------------------------------------------------------------------------

    private suspend fun update(note: NoteEntry) {
        withContext(Dispatchers.IO) {
            dataSource.updateNote(note)
        }
    }

    fun updateNotes(fromNote: NoteEntry, toNote: NoteEntry, fromPosition: Int, toPosition: Int) {
        viewModelScope.launch {
            fromNote.notePosition = toPosition
            toNote.notePosition = fromPosition

            update(fromNote)
            update(toNote)
        }
    }

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

    private val _navigateToNewNote = MutableLiveData<NoteEntry>()
    val navigateToNewNote: LiveData<NoteEntry>
        get() = _navigateToNewNote

    private suspend fun insertNewNote(note: NoteEntry) {
        withContext(Dispatchers.IO) {
            dataSource.insertNewNote(note)
        }
    }

    private suspend fun updateNotePosition(noteID: Long, note: NoteEntry) {
        withContext(Dispatchers.IO) {
            note.notePosition = noteID.toInt()
            dataSource.updateNote(note)
        }
    }

    fun onAddNote(
        noteEntryBackgroundColor: Int,
        noteEntryTextColor: Int,
        noteEntryNoteSize: Float,
        noteEntryTitleSize: Float,

        ) {
        viewModelScope.launch {

            val newNote = NoteEntry(
                noteEntryTitle = "",
                noteEntryNote = "",
                noteEntryBackgroundColor = noteEntryBackgroundColor.toString().toInt(),
                noteEntryTextColor = noteEntryTextColor.toString().toInt(),
                noteEntryNoteTextSize = noteEntryNoteSize,
                noteEntryTitleTextSize = noteEntryTitleSize,
            )

            insertNewNote(newNote)

            Timber.i("Timber: After update note position ${newNote}")

            _navigateToNewNote.value = getLatestNoteFromDatabase()

            Timber.i("Timber: navigatetonewntoe ${_navigateToNewNote.value}")

            _navigateToNewNote.value?.notePosition = _navigateToNewNote.value?.noteId?.toInt()!!

            updateNotePosition(_navigateToNewNote.value?.noteId!!, _navigateToNewNote.value!!)

            Timber.i("Timber: navigatetonewfragment ${_navigateToNewNote.value}")

        }
    }
}