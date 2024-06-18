package com.ekh.notlarm.presentation.notes.composables

sealed class screen (val route: String) {
    object notesScreen : screen("notes_screen")
    object addEditScreen : screen("add_edit_screen")
}