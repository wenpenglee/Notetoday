package com.wenpenglee.notetoday.data
import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.wenpenglee.notetoday.AndroidContextProvider

actual fun getDatabaseBuilder(): RoomDatabase.Builder<NoteDatabase> {
    val context = AndroidContextProvider.appContext
    val dbFile = context.getDatabasePath("notes.db")
    return Room.databaseBuilder<NoteDatabase>(
        context = context,
        name = dbFile.absolutePath
    )
}
