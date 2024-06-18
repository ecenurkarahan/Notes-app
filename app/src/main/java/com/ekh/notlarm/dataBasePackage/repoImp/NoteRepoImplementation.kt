package com.ekh.notlarm.dataBasePackage.repoImp

import com.ekh.notlarm.domain.noteClass.note
import com.ekh.notlarm.domain.repository.NoteRepository
import com.ekh.notlarm.dataBasePackage.source.NoteDao
import kotlinx.coroutines.flow.Flow

class NoteRepoImplementation (
    private val dao: NoteDao
) : NoteRepository {
    override suspend fun getNotewithId(id: Int): note? {
        return dao.getNotewithId(id)
    }
    override fun getAllNotes(): Flow<List<note>> {
        return dao.getAllNotes()
    }

    override suspend fun deleteNote(theNote: note) {
        return dao.deleteNote(theNote)
    }
    override suspend fun insertNote(theNote: note) {
        return dao.insertNote(theNote)
    }
    override fun searchNotes(query: String): Flow<List<note>> {
        return dao.searchNotes(query)
    }

}