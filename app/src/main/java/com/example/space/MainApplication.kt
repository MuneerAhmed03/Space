package com.example.space

import android.app.Application
import com.example.space.data.NoteRoomDatabase

class MainApplication : Application(){

    val database : NoteRoomDatabase by lazy {
        NoteRoomDatabase.getDatabase(this)
    }

}