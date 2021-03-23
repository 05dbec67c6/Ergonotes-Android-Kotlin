package com.ergonotes.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.launch

class EditViewModel(
    private val noteEntryKey: Long = 0L,
    private val dataBase: NoteEntryDao
) : ViewModel() {

    private val note: LiveData<NoteEntry> = dataBase.getNoteWithId(noteEntryKey)
    fun getNote() = note

// -------------------------------------------------------------------------------------------------
// update note from edit------- --------------------------------------------------------------------

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
}


