package com.ergonotes.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergonotes.database.NoteEntry
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.launch

class DialogDeleteViewModel(
    private val noteEntryKey: Long = 0L,
    private val dataSource: NoteEntryDao
) : ViewModel() {

// -------------------------------------------------------------------------------------------------
// Delete target note-------------------------------------------------------------------------------

    private suspend fun deleteTargetNote(note: NoteEntry) {
        dataSource.deleteTargetNote(note)
    }

    fun deleteTargetNote() {
        viewModelScope.launch {
            val note = dataSource.getTargetNote(noteEntryKey)
            deleteTargetNote(note)
        }
    }
}


