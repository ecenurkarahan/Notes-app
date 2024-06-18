package com.ekh.notlarm.presentation.notes

import com.ekh.notlarm.domain.noteClass.note

data class NotesState(
    val notes: List<note> = emptyList(),
    val theOrder: noteOrderType = noteOrderType.Date(orderType.Descending),
    val isOrderSectionVisible : Boolean = false
)
