package com.ergonotes.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ergonotes.database.NoteEntryDao
import com.ergonotes.viewmodels.EditViewModel

class EditViewModelFactory(
    private val noteEntryKey: Long,
    private val dataBase: NoteEntryDao
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            return EditViewModel(noteEntryKey, dataBase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}