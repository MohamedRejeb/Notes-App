package com.mohamedbenrejeb.notesapp

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun HomeScreen() {
    val context = LocalContext.current

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
                        title = title.value,
                        description = description.value
                    )

                    // Create mutable copy of List
                    val mutableNotes = notes.value.toMutableList()
                    // Add note to the new mutable list
                    mutableNotes.add(note)
                    // Update notes list state with the new mutable list
                    notes.value = mutableNotes
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

            notes.value.forEach { note ->
                Card(
                    backgroundColor = Color.Red
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
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