package com.ergonotes.viewmodelfactories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ergonotes.database.NoteEntryDao
import com.ergonotes.viewmodels.NewViewModel

class NewViewModelFactory(
    private val noteEntryKey: Long,
    private val dataSource: NoteEntryDao,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewViewModel::class.java)) {
            return NewViewModel(noteEntryKey, dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
