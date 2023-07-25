 package com.example.space.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract val noteDao: NotesDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase?=null
        fun getDatabase(context: Context): NoteRoomDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "item_database"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE=instance
                return instance
            }
        }
    }
}