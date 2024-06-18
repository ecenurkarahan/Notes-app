package com.ekh.notlarm.domain.useCases

import com.ekh.notlarm.domain.noteClass.note
import com.ekh.notlarm.domain.repository.NoteRepository

class GetNote (
    private val repository : NoteRepository
){
    suspend operator fun invoke(id: Int): note? {
        return repository.getNotewithId(id)
    }
}