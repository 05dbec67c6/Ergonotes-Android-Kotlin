package com.ergonotes.viewmodels

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.launch

class EditViewModel(
    private val noteEntryKey: Long = 0L,
    private val dataBase: NoteEntryDao
) : ViewModel() {

// -------------------------------------------------------------------------------------------------
// Set and initialize-------------------------------------------------------------------------------

    private val note: LiveData<NoteEntry> = dataBase.getNoteWithId(noteEntryKey)
    fun getNote() = note


    private var newestNote = MutableLiveData<NoteEntry?>()
    private suspend fun insertNewNote(note: NoteEntry) {
        dataBase.insertNewNote(note)
    }
    private suspend fun getLatestNoteFromDatabase(): NoteEntry? {
        return dataBase.getLatestNoteFromDatabase()
    }

    val allNotes = dataBase.getAllNotes()
// -------------------------------------------------------------------------------------------------
// Button apply update database --------------------------------------------------------------------

    fun onSetNote(backgroundColor: Int, textColor: Int, noteTextSize: Float, titleTextSize: Float) {
        viewModelScope.launch {
            val note = dataBase.getTargetNote(noteEntryKey)
            note.noteEntryBackgroundColor = backgroundColor
            note.noteEntryTextColor = textColor
            note.noteEntryNoteTextSize = noteTextSize
            note.noteEntryTitleTextSize = titleTextSize
            dataBase.updateNote(note)
        }
    }


    // -------------------------------------------------------------------------------------------------
// Button update database --------------------------------------------------------------------------
    private suspend fun deleteTargetNote(note: NoteEntry) {
        dataBase.deleteTargetNote(note)
    }

    fun deleteTargetNote() {
        viewModelScope.launch {
            val note = dataBase.getTargetNote(noteEntryKey)
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


