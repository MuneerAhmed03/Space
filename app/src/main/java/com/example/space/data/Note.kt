package com.example.space.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Note(
    val title  : String,
    val content : String,
    val timestamp: Long,
    val date: LocalDate,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
