package com.example.space.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Delete
    fun deleteNotes(entities: List<Note>)

    @Update
    suspend fun updateNote(note: Note)

    @Query("SELECT * FROM note")
    fun getNotes() : Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id= :id")
    fun getNotebyId(id:Long): LiveData<Note>
    @Query("SELECT * FROM note WHERE date = :targetDate")
     fun getNotesByDate(targetDate: String): Flow<List<Note>>
}