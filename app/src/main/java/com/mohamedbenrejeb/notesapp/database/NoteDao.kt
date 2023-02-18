package com.mohamedbenrejeb.notesapp.database

import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    suspend fun addNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note_table")
    suspend fun getAllNotes(): List<Note>

}