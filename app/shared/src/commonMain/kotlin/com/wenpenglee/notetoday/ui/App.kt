package com.wenpenglee.notetoday.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    val db = remember {
        getDatabaseBuilder()
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(true)
            .build()
    }
    val noteDao = remember { db.noteDao() }
    val scope = rememberCoroutineScope()

    MaterialTheme {
        var contentInput by remember { mutableStateOf("") }
        var titleInput by remember { mutableStateOf("") }
        val notes by noteDao.queryAllNotes().collectAsState(initial = emptyList())
        var editingNoteId by remember { mutableStateOf<String?>(null) }
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "My Note",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                TextField(
                    value = titleInput,
                    label = { Text("標題") },
                    onValueChange = { titleInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = contentInput,
                    label = { Text("內容") },
                    onValueChange = { contentInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (titleInput.isBlank() && contentInput.isBlank()) {
                            return@Button
                        }
                        scope.launch {
                            if (editingNoteId != null) {
                                val existingNote = notes.find { it.id == editingNoteId }
                                existingNote?.let {
                                    noteDao.updateNote(
                                        it.copy(
                                            title = titleInput,
                                            content = contentInput
                                        )
                                    )
                                }
                                editingNoteId = null
                            } else {
                                noteDao.insertNote(
                                    Note(
                                        id = Uuid.random().toString(),
                                        title = titleInput,
                                        content = contentInput
                                    )
                                )
                            }
                            titleInput = ""
                            contentInput = ""
                        }
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(if (editingNoteId != null) "Save" else "Add")
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 160.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    notes,
                    key = { it.id }
                ) { note ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column (modifier = Modifier.padding(16.dp)) {

                            Text(
                                note.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                note.content,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row (horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                                IconButton(onClick = {
                                    editingNoteId = note.id
                                    titleInput = note.title
                                    contentInput = note.content
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "編輯"
                                    )
                                }
                                IconButton(onClick = {
                                    scope.launch { noteDao.deleteNote(note) }
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "刪除")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

