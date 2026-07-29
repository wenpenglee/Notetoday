package com.wenpenglee.notetoday.data

import androidx.room3.Room
import androidx.room3.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<NoteDatabase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "notes.db")
    return Room.databaseBuilder<NoteDatabase>(
        name = dbFile.absolutePath
    )
}