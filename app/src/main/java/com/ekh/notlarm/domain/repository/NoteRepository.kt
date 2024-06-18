package com.ekh.notlarm.domain.repository

import com.ekh.notlarm.domain.noteClass.note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<note>>
    suspend fun getNotewithId(id:Int) : note?

    suspend fun insertNote(theNote: note)
    fun searchNotes(query: String): Flow<List<note>>
    suspend fun deleteNote(theNote: note)
}