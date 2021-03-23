package com.ergonotes.viewmodelfactories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ergonotes.database.NoteEntryDao
import com.ergonotes.viewmodels.DialogDeleteViewModel

class DialogDeleteViewModelFactory(

    private val noteEntryKey: Long,
    private val dataSource: NoteEntryDao

) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DialogDeleteViewModel::class.java)) {
            return DialogDeleteViewModel(noteEntryKey, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
