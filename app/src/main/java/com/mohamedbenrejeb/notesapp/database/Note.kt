package com.mohamedbenrejeb.notesapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String
)