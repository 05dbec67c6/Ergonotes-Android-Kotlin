package com.ergonotes.newfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ergonotes.database.NoteEntryDao


class NewFragmentViewModelFactory(
    private val noteEntryKey: Long,
    private val dataSource: NoteEntryDao) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewFragmentViewModel::class.java)) {
            return NewFragmentViewModel(noteEntryKey, dataSource) as T
        }
        //noteEntryKey, dataSource
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
