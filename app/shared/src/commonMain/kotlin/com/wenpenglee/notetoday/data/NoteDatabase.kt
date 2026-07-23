package com.wenpenglee.notetoday.data

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}