package com.ekh.notlarm.presentation.addEditNotes

import androidx.compose.ui.focus.FocusState

sealed class AddEditEvent {
    data class EnteredTitle(val newTitle:String):AddEditEvent()
    data class ChangeTitleFocus(val focusState: FocusState): AddEditEvent()
    data class ColorChange(val newColor:Int):AddEditEvent()
    object SaveNote: AddEditEvent()
    data class EnteredContent(val newContent:String):AddEditEvent()
    data class ChangeContentFocus(val focusState: FocusState): AddEditEvent()
}