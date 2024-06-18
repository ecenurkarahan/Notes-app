package com.ekh.notlarm.presentation.notes.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ekh.notlarm.domain.noteClass.note

@Composable
fun NoteComposable(
    theNote: note,
    modifier: Modifier = Modifier,
    onDeleteClick : ()->Unit
){
    Box (modifier=modifier) {
        Column (modifier= Modifier
            .fillMaxSize()
            .padding(end = 32.dp)
            .padding(16.dp))
        {
            Text(text =theNote.theTitle,
                style = MaterialTheme.typography.headlineMedium,
                overflow = TextOverflow.Ellipsis,
                color= MaterialTheme.colorScheme.onSurface,
                maxLines = 1,)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text =theNote.theNoteContent,
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                color= MaterialTheme.colorScheme.onSurface,
                maxLines = 8)
        }
        IconButton(onClick = onDeleteClick,
            modifier= Modifier.align(Alignment.BottomEnd)) {
            Icon(imageVector = Icons.Default.Delete,
                contentDescription =  "Notu Siler" )

        }
    }
}