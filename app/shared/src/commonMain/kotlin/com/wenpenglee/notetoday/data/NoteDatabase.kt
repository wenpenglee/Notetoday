package com.wenpenglee.notetoday.data

import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.TypeConverters

@Database(entities = [Note::class], version = 1)
@TypeConverters(Converters::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun noteDao(): NoteDao
}