package com.ekh.notlarm.presentation.addEditNotes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekh.notlarm.domain.noteClass.InvalidNoteException
import com.ekh.notlarm.domain.noteClass.note
import com.ekh.notlarm.domain.useCases.NotesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class AddEditViewModel@Inject constructor(
    private val noteUseCases: NotesUseCases,
    savedStateHandle : SavedStateHandle
) : ViewModel()  {
    private val _theContent = mutableStateOf(
        NoteFieldState(
            defaultText = "Enter the note..."
        )
    )
    val noteContent: State<NoteFieldState> = _theContent
    private val _theTitle = mutableStateOf(
        NoteFieldState(
            defaultText = "Enter the Title..."
        )
    )
    val noteTitle: State<NoteFieldState> = _theTitle
    private val _theDate = mutableStateOf(0L)
    val noteDate: State<Long> = _theDate
    private val _theColor = mutableStateOf<Int>(note.colorsAvailable.random().toArgb())
    val noteColor: State<Int> = _theColor
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()
    private var apparentId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNote(noteId)?.also { note ->
                        apparentId = note.id
                        _theTitle.value = noteTitle.value.copy(
                            text = note.theTitle,
                            isDefault = false,

                            )
                        _theContent.value = noteContent.value.copy(
                            text = note.theNoteContent,
                            isDefault = false
                        )
                        _theColor.value = note.color
                        _theDate.value = note.timeValue
                    }
                }
            }
        }
    }
    fun eventFunction(event: AddEditEvent) {
        when (event) {
            is AddEditEvent.EnteredTitle -> {
                _theTitle.value = noteTitle.value.copy(text = event.newTitle)
            }
            is AddEditEvent.EnteredContent -> {
                _theContent.value = noteContent.value.copy(text = event.newContent)
            }

            is AddEditEvent.ChangeTitleFocus -> {
                _theTitle.value = noteTitle.value.copy(
                    isDefault = !event.focusState.isFocused &&
                            noteTitle.value.text.isBlank()
                )
            }


            is AddEditEvent.ChangeContentFocus -> {
                _theContent.value = noteContent.value.copy(
                    isDefault = !event.focusState.isFocused &&
                            noteContent.value.text.isBlank()
                )
            }
            is AddEditEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.addNoteUseCase(
                            note(
                                theTitle = noteTitle.value.text,
                                theNoteContent = noteContent.value.text,
                                timeValue = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = apparentId
                            )
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackBar(
                                message = e.message ?: "Saving is not succesful."
                            )
                        )
                    }
                }
            }

            is AddEditEvent.ColorChange -> {
                _theColor.value = event.newColor
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}