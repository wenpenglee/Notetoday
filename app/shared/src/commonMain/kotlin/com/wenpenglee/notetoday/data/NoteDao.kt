package com.wenpenglee.notetoday.data

import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

interface NoteDao {
    @Insert
    fun insertNote(note: Note)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT * FROM notes")
    fun queryAllNotes():Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun queryNoteWithId(id: Uuid): Note?
}