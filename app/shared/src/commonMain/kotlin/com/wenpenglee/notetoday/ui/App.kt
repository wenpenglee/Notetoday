package com.wenpenglee.notetoday.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.wenpenglee.notetoday.data.Note

import kotlin.uuid.Uuid

@Composable
@Preview
fun App() {
    MaterialTheme {
        var inputText by remember { mutableStateOf("") }
        val notes = remember { mutableStateListOf<Note>() }
        var editingNoteId by remember { mutableStateOf<Uuid?>(null) }
        Column {
            Text("My Note")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(value = inputText, label = { Text("Title") }, onValueChange = {
                    inputText = it
                })
                Button(onClick = {
                    if (inputText == "") {
                        print("Empty input")
                        return@Button
                    }
                    if (editingNoteId != null) {
                        val index = notes.indexOfFirst { it.id == editingNoteId }
                        notes[index] = notes[index].copy(content = inputText)
                        editingNoteId = null
                    } else {
                        notes.add(Note(content = inputText, id = Uuid.random()))
                    }
                    inputText = ""
                }) {
                    if (editingNoteId != null) {
                        Text("Save")
                    } else {
                        Text("Add")
                    }
                }
            }

            LazyColumn {
                items(
                    notes.size,
                    key = { index ->
                        notes[index].id
                    }
                ) { index ->
                    val note = notes[index]
                    Card() {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
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
                                    notes.remove(note)
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

