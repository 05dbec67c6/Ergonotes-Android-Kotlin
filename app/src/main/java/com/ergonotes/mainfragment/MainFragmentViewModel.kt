package com.ergonotes.mainfragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ergonotes.database.NoteEntryDao

class MainFragmentViewModel(
    val database: NoteEntryDao,
    application: Application) : AndroidViewModel(application) {

}



