package com.ekh.notlarm.domain.useCases

import com.ekh.notlarm.domain.noteClass.InvalidNoteException
import com.ekh.notlarm.domain.noteClass.note
import com.ekh.notlarm.domain.repository.NoteRepository
import kotlin.jvm.Throws

class AddNoteUseCase(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(theNote: note){
        if(theNote.theNoteContent.isBlank()){
            throw InvalidNoteException("The content of the note can not be empty, please check!")
        }
        if(theNote.theTitle.isBlank()){
            throw InvalidNoteException("The title of the note can not be empty, please check!")
        }
        repository.insertNote(theNote)
    }
}