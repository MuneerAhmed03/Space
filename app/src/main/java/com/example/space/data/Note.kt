package com.example.space.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Note(
    val title  : String,
    val content : String,
    val date: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L
)
