package com.ergonotes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteEntry::class], version = 2, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract val noteDatabaseDao: NoteEntryDao

    // Companion object, because no instance of the class is needed
    companion object {

        //Volatile means, it will never get cached
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        // returns reference to the database
        fun getDatabase(context: Context): NoteDatabase {

            // only one thread can enter the code at a time
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {

                    // build the database
                    instance = Room.databaseBuilder(

                        // supply context
                        context.applicationContext,

                        // tell which database
                        NoteDatabase::class.java,

                        // give it a name
                        "note_entries_database"
                    )
                        //Immigration object, more info, look it up
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}