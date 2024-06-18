package com.ekh.notlarm.presentation.addEditNotes

data class NoteFieldState (
    val text : String = "",
    val isDefault : Boolean = true,
    val defaultText : String =""
)