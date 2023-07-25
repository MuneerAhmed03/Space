package com.example.space.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT * FROM note")
    fun getNotes() : Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id= :id")
    suspend fun getNotebyId(id:Int): Note?

    @Query("SELECT * FROM note WHERE date = :targetDate")
    fun getEntityByDate(targetDate: LocalDate): Note?
}