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
import java.util.concurrent.Executors


class MainViewModel(
    private val dataSource: NoteEntryDao
) :
    ViewModel() {

    val allNotesByPosition = dataSource.getAllNotesByPosition()

    private val _navigateToNewFragment = MutableLiveData<NoteEntry>()
    val navigateToNewFragment: LiveData<NoteEntry>
        get() = _navigateToNewFragment

// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// The following contains some experimental things in order to update the positions of the
// two items in the MainFragment

    // use suspendfunction in order to make Dao work
    private suspend fun update(note: NoteEntry) {
        withContext(Dispatchers.IO) {
            dataSource.updateNote(note)
        }
    }

    // an attempt to get the Note by the position, in order to update it
    private suspend fun getTargetNoteByPos(position: Int) {
        withContext(Dispatchers.IO) {
            dataSource.getTargetNoteByPosition(position)
        }
    }

    // normal updating
    fun updateNote(note: NoteEntry) {
        viewModelScope.launch {
            update(note)
        }
    }

    // attempt with two given notes and two positions
    fun updateNotes(fromNote: NoteEntry, toNote: NoteEntry, fromPosition: Int, toPosition: Int) {
        viewModelScope.launch {
            fromNote.notePosition = toPosition
            toNote.notePosition = fromPosition

            val newFromNote = fromNote.copy()
            newFromNote.notePosition = toNote.notePosition

            val newToNote = toNote.copy()
            newToNote.notePosition = fromNote.notePosition

            update(newFromNote)
            update(newToNote)
        }
    }

    val dummyNoteEntry: NoteEntry = NoteEntry()

    fun getTargetNoteByPosition1(position: Int): NoteEntry {
        viewModelScope.launch {
            dataSource.getTargetNoteByPosition(position)
        }
        return dummyNoteEntry
    }

    // another failed attempt on creating a copy of the given list, because I read somewhere,
    // that this is necessary, because the updatefunction will not trigger the livedata to change
        fun swapItem(from: Int, to: Int) {

        Executors.newSingleThreadExecutor().execute {

            val item1: NoteEntry = getTargetNoteByPosition1(from)
            val item2 = getTargetNoteByPosition1(to)

            Timber.i("$item1, $item2")

            // Swap index of 2 items

            val newItem1 = item1.copy()
            newItem1.notePosition = item2.notePosition

            val newItem2 = item2.copy()
            newItem2.notePosition = item1.notePosition

            updateNote(newItem1)
            updateNote(newItem2)
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

            Timber.i("InsertNewNoteValue: ${newNote}")

            _navigateToNewNote.value = getLatestNoteFromDatabase()

            Timber.i("_navigateToNewNote.value before updateNotePosition = ${_navigateToNewNote.value}")

            _navigateToNewNote.value?.notePosition = _navigateToNewNote.value?.noteId?.toInt()!!

            updateNotePosition(_navigateToNewNote.value?.noteId!!, _navigateToNewNote.value!!)

            Timber.i("_navigateToNewNote.value after updateNotePosition ${_navigateToNewNote.value}")

        }
    }
}