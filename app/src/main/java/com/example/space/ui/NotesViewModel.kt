package com.example.space.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.space.data.Note
import com.example.space.data.NoteRepository
import com.example.space.data.NoteRoomDatabase
import kotlinx.coroutines.launch

class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val repo:NoteRepository

    private val _noteId= MutableLiveData<Long>(0)
    val noteId: LiveData<Long> get() = _noteId



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

    fun isEntryValid(title:String):Boolean{
        if(title.isBlank()){
            return false
        }
    return true
    }
    fun deleteNote(note: Note){
        viewModelScope.launch {
            repo.deleteNote(note)
        }
    }
    fun deleteNotes(entities: List<Note>) {
        repo.deleteNotes(entities)
    }
    fun getTodayNotes(date: String) : LiveData<List<Note>> {
        return repo.getNoteByDate(date)
    }
    fun getNotesById(id:Long): LiveData<Note> {
        return  repo.getNoteById(id)
    }
}
