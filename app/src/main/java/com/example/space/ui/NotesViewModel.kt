package com.example.space.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.space.MainApplication
import com.example.space.data.Note
import com.example.space.data.NotesDao
import com.example.space.data.NoteRepository
import com.example.space.data.NoteRoomDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.io.InvalidClassException

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val repo:NoteRepository

    init {
        val dao=NoteRoomDatabase.getDatabase(application).notesDao()
        repo=NoteRepository(dao)
    }
    private fun insertNote(note: Note){
        viewModelScope.launch {
            repo.insertNote(note)
        }
    }
    private fun getNewNote(title:String,content:String,date:String):Note{
        return Note(
            title = title,
            content = content,
            date=date
        )
    }
    fun addNote(title:String,content:String,date:String){
        val newNote=getNewNote(title,content,date)
        insertNote(newNote)
    }

    private fun updateNote(note: Note){
        viewModelScope.launch {
            repo.updateNote(note)
        }
    }
    private fun getUpdatedNoteEntry(
        noteId: Long,
        title: String,
        content: String,
        date: String
    ): Note {
        return Note(
            id = noteId,
            title = title,
            content = content,
            date = date
        )
    }
    fun updateNote(
        noteId: Long,
        title: String,
        content: String,
        note: String
    ) {
        val updatedNote = getUpdatedNoteEntry(noteId, title, content, note)
        updateNote(updatedNote)
    }

//    fun showNotes(date: String):LiveData<List<Note>>{
//        val allNote: LiveData<List<Note>> = repo.getNoteByDate(date).asLiveData()
//        return allNote
//    }
    fun deleteNote(note: Note){
        viewModelScope.launch {
            repo.deleteNote(note)
        }
    }
    fun getTodayNotes(date: String) : LiveData<List<Note>> {
        return repo.getNoteByDate(date)
    }
}
