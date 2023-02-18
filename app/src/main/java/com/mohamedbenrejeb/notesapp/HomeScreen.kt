package com.mohamedbenrejeb.notesapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.mohamedbenrejeb.notesapp.database.Note
import com.mohamedbenrejeb.notesapp.database.NoteDatabase
import kotlinx.coroutines.*
import java.util.UUID

@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val db = remember {
        Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note_database"
        ).build()
    }

    val title = remember {
        mutableStateOf("")
    }
    val description = remember {
        mutableStateOf("")
    }

    val notes = remember {
        mutableStateOf(emptyList<Note>())
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = Unit) {
        val newNotes = db.noteDao().getAllNotes()
        notes.value = newNotes
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(
                all = 20.dp,
            )
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
        ) {
            Text(
                text = "Add note:",
                style = MaterialTheme.typography.h5,
                textDecoration = TextDecoration.Underline
            )

            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = title.value,
                onValueChange = { newValue ->
                    title.value = newValue
                },
                placeholder = {
                    Text(text = "Title")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            TextField(
                value = description.value,
                onValueChange = { newValue ->
                    description.value = newValue
                },
                placeholder = {
                    Text(text = "Description")
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    // Check note title and description
                    val errorMessage =
                        if (title.value.isBlank())
                            "Title can't be blank"
                        else if (title.value.length < 3)
                            "Title is too short"
                        else if (description.value.isBlank())
                            "Description can't be blank"
                        else if (description.value.length > 100)
                            "Description is too long"
                        else
                            ""

                    if (errorMessage.isNotEmpty()) {
                        Toast.makeText(
                            context,
                            errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    val note = Note(
                        id = UUID.randomUUID().toString(),
                        title = title.value,
                        description = description.value
                    )

                    scope.launch {
                        // Add note to database
                        db.noteDao().addNote(note)
                        // Get all notes from database
                        notes.value = db.noteDao().getAllNotes()
                    }

                    title.value = ""
                    description.value = ""
                }
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "add"
                )

                Text(text = "Add Note")
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "All notes:",
                style = MaterialTheme.typography.h5,
                textDecoration = TextDecoration.Underline
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (notes.value.isEmpty()) {
                Text(
                    text = "There is no notes."
                )
            }

            // Notes Column
            notes.value.forEach { note ->
                Card(
                    backgroundColor = Color.DarkGray
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(all = 10.dp)
                        ) {
                            Text(
                                text = note.title,
                                style = MaterialTheme.typography.h6,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = note.description,
                                style = MaterialTheme.typography.body1,
                                color = Color.White
                            )
                        }

                        IconButton(onClick = {
                            scope.launch {
                                // Delete note from database
                                db.noteDao().deleteNote(note)
                                // Update notes state with the latest notes from database
                                notes.value = db.noteDao().getAllNotes()
                            }
                        }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "delete note",
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}