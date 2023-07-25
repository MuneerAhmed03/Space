package com.example.space.data

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
    suspend fun getNoteById(id : Int) : Note?{
        return notesDao.getNotebyId(id)
    }
    suspend fun getNoteByDate(targetDate: LocalDate):Flow<List<Note>>{
        return notesDao.getNotesByDate(targetDate)
    }



}