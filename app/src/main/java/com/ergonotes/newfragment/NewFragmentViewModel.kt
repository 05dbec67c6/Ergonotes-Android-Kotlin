package com.ergonotes.newfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ergonotes.database.NoteEntryDao
import kotlinx.coroutines.Job

class NewFragmentViewModel(
    private val sleepNightKey: Long = 0L,
    val database: NoteEntryDao
) : ViewModel() {

    private val viewModelJob = Job()

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
}