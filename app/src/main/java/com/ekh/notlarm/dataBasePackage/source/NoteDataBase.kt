package com.ekh.notlarm.dataBasePackage.source

import androidx.room.Database
import com.ekh.notlarm.domain.noteClass.note

@Database(
    entities = [note::class],
    version = 1
)
abstract class NoteDataBase {
    abstract val noteDao: NoteDao
    companion object {
        const val DATABASE_NAME ="notes_db"
    }
}