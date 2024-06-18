package com.ekh.notlarm.dataBasePackage.source

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ekh.notlarm.domain.noteClass.note
import kotlinx.coroutines.flow.Flow

interface NoteDao {
    @Query("SELECT * FROM note WHERE theTitle LIKE '%' || :query || '%' OR theNoteContent LIKE '%' || :query || '%' ORDER BY timeValue DESC")
    fun searchNotes(query: String): Flow<List<note>>
    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<note>>
    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNotewithId(id:Int) : note?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    //if we call it with an already existing id, it will just change the existing note
    suspend fun insertNote(theNote: note)
    @Delete
    suspend fun deleteNote(theNote: note)
}