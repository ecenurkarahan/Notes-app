package com.ekh.notlarm.domain.noteClass

import androidx.room.PrimaryKey
import com.ekh.notlarm.ui.theme.*

data class note( val theTitle: String,
                 val theNoteContent: String,
                 val color: Int,
                 val timeValue: Long,
                 @PrimaryKey val id: Int? =null){
    companion object{
        val colorsAvailable = listOf(
            yellow,
            mintGreen,
            lightBlue,
            lila,
            lightPink,
            lightGreen)
    }
    //i made it searchable by the note content and title, it shows all matching notes
    fun doesMatchQuery(query: String): Boolean{
        val matchingCombs= listOf(
            theTitle,
            theNoteContent
        )
        return matchingCombs.any{
            it.contains(query, ignoreCase = true)
        }
    }
}
class InvalidNoteException(message: String) :Exception(message)
