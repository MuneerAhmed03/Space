package com.example.space.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class NoteRepository(private val notesDao: NotesDao) {
    suspend fun insertNote(note: Note){
        return notesDao.insertNote(note)
    }
    suspend fun deleteNote(note: Note){
        return notesDao.deleteNote(note)
    }
    suspend fun updateNote(note: Note){
        return notesDao.updateNote(note)
    }
    fun getNotes(): Flow<List<Note>>{
        return notesDao.getNotes()
    }
    fun getNoteById(id : Long) : Note?{
        return notesDao.getNotebyId(id)
    }
    fun getNoteByDate(targetDate: String):LiveData<List<Note>>{
        return notesDao.getNotesByDate(targetDate).asLiveData()
    }



}