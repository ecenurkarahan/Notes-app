package com.ekh.notlarm.domain.useCases

import com.ekh.notlarm.domain.noteClass.note
import com.ekh.notlarm.domain.repository.NoteRepository

class deleteNoteUseCase (val repository: NoteRepository
) {
    suspend operator fun invoke(theNote: note) {
        repository.deleteNote(theNote)
    }
}
