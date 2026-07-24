package com.wenpenglee.notetoday.data

import androidx.room3.RoomDatabase


expect fun getDatabaseBuilder(): RoomDatabase.Builder<NoteDatabase>