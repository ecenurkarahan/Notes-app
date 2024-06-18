package com.ekh.notlarm.domain.useCases

data class NotesUseCases(val getNote: GetNote,
                         val searchNotesUseCase: SearchNotesUseCase,
                         val deleteNoteUseCase: deleteNoteUseCase,
                         val addNoteUseCase: AddNoteUseCase
)
