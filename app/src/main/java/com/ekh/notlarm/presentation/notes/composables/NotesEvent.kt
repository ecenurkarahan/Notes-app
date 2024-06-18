package com.ekh.notlarm.presentation.notes

sealed class NotesEvent {
    object RestoreNote :NotesEvent()
    data class Order( val theOrder: noteOrderType): NotesEvent()
    data class DeleteNote (val theNote:note) :NotesEvent()

    data class SearchNotes(val query: String) : NotesEvent()
}