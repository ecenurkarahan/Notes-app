package com.ekh.notlarm.domain.useCases

import com.ekh.notlarm.domain.noteClass.note
import com.ekh.notlarm.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class SearchNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(query: String): Flow<List<note>> {
        return repository.searchNotes(query)
    }
}