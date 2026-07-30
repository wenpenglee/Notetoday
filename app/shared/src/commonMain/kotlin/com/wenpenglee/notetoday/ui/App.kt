package com.wenpenglee.notetoday.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.wenpenglee.notetoday.data.Note
import com.wenpenglee.notetoday.data.getDatabaseBuilder
import kotlinx.coroutines.launch

import kotlin.uuid.Uuid

@Composable
@Preview
fun App() {
    val db = remember { getDatabaseBuilder().setDriver(BundledSQLiteDriver()).build() }
    val noteDao = remember { db.noteDao() }
    val scope = rememberCoroutineScope()
    MaterialTheme {
        var inputText by remember { mutableStateOf("") }
        val notes by noteDao.queryAllNotes().collectAsState(initial = emptyList())
        var editingNoteId by remember { mutableStateOf<String?>(null) }
        Column {
            Text("My Note")

            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(value = inputText, label = { Text("Title") }, onValueChange = {
                    inputText = it
                })
                Button(onClick = {
                    if (inputText.isBlank()) {
                        print("Empty input")
                        return@Button
                    }
                    scope.launch {
                        if (editingNoteId != null) {
                            val existingNote = notes.find { it.id == editingNoteId }
                            existingNote?.let {
                                noteDao.updateNote(existingNote.copy(content = inputText))
                            }
                            editingNoteId = null
                        } else {
                            noteDao.insertNote(Note(id = Uuid.random().toString(), content = inputText))
                        }
                        inputText = ""
                    }
                }) {
                    if (editingNoteId != null) {
                        Text("Save")
                    } else {
                        Text("Add")
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    notes.size,
                    key = { index ->
                        notes[index].id
                    }
                ) { index ->
                    val note = notes[index]
                    Card() {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(note.content)
                            Row {
                                IconButton(onClick = {
                                    editingNoteId = note.id
                                    inputText = note.content
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "編輯"
                                    )
                                }
                                IconButton(onClick = {
                                    scope.launch {
                                        noteDao.deleteNote(note)
                                    }
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "刪除"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

