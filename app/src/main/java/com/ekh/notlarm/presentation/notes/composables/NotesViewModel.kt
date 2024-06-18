package com.ekh.notlarm.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekh.notlarm.domain.noteClass.note
import com.ekh.notlarm.domain.useCases.NotesUseCases
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val noteUseCases: NotesUseCases
): ViewModel() {
    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state
    private var lastDeletedNote: note? = null
    private val _searchText = MutableStateFlow("")
    val searchText= _searchText.asStateFlow()
    private val _isSearching= MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()
    private val _notes = MutableStateFlow(listOf<note>())
    val notes = searchText
        .combine(_notes){ text, notes->
            if(text.isBlank()){
                notes
            }
            else{
                notes.filter {
                    it.doesMatchQuery(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _notes.value
        )



    private var  getNotesJobe: Job?= null
    init{
        getAllNotes(noteOrderType.Date(orderType.Descending))
    }
    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNoteUseCase(event.theNote)
                    lastDeletedNote = event.theNote
                }
            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.addNoteUseCase(lastDeletedNote ?: return@launch)
                    lastDeletedNote = null
                }
            }
            is NotesEvent.Order -> {
                if (state.value.theOrder::class == event.theOrder::class && state.value.theOrder.orderType == event.theOrder.orderType) {
                    return
                }
                getAllNotes(event.theOrder)

            }
            is NotesEvent.SearchNotes -> {
                searchNotes(event.query)
            }

        }
    }
    private fun searchNotes(query: String) {
        viewModelScope.launch {
            noteUseCases.searchNotesUseCase(query).collect { notes ->
                _notes.value = notes
                _state.value = state.value.copy(notes = notes)
            }
        }
    }

    private fun getAllNotes(theOrder: noteOrderType) {
        getNotesJobe?.cancel()
        getNotesJobe= noteUseCases.getAllNotesUseCase(theOrder).onEach {notes->
            _state.value= state.value.copy(notes=notes,
                theOrder= theOrder
            )
        }
            .launchIn(viewModelScope)
    }
    fun onSearchTextChange(text: String){
        _searchText.value=text
        onEvent(NotesEvent.SearchNotes(text))
    }
}