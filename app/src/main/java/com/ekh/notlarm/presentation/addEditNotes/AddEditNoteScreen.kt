package com.ekh.notlarm.presentation.addEditNotes

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ekh.notesofserife.R
import com.ekh.notlarm.domain.noteClass.note
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditViewModel= hiltViewModel(),
    noteColor: Int
){
    val contentState = viewModel.noteContent.value
    val dateState = viewModel.noteDate.value
    val titleState= viewModel.noteTitle.value
    val scaffoldState = rememberBottomSheetScaffoldState()
    val noteBackgroundAnim= remember{
        Animatable(
            Color(if (noteColor!=-1) noteColor else viewModel.noteColor.value)
        )
    }
    val theScope = rememberCoroutineScope()
    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event ->
            listOf(event).forEach {
                if (it is AddEditViewModel.UiEvent.SaveNote) {
                    navController.navigateUp()
                } else if (it is AddEditViewModel.UiEvent.ShowSnackBar) {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = it.message
                    )
                }
            }
        }
    }
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    val datePart = remember(dateState) {
        LocalDateTime.ofEpochSecond(dateState / 1000, 0, ZoneOffset.UTC).format(dateFormatter)
    }
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(onClick =
            {
                viewModel.eventFunction(AddEditEvent.SaveNote)
            },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White,
                modifier = Modifier.background(MaterialTheme.colorScheme.primary)
            ) {
                val saveFile: ImageVector = ImageVector.vectorResource(id = R.drawable.save_file)
                Icon(imageVector = saveFile, contentDescription = "Saving the note",  modifier = Modifier.size(24.dp))
            }
        }
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnim.value)
                .padding(16.dp)
        ){
            Row (modifier= Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ){
                note.colorsAvailable.forEach { color ->
                    val colorInt = color.toArgb()
                    ColorElement(
                        isSelected = viewModel.noteColor.value == color.toArgb(),
                        color = color,
                        onColorSelected = {
                            theScope.launch {
                                noteBackgroundAnim.animateTo(
                                    targetValue = color,
                                    animationSpec = tween(durationMillis = 500)
                                )
                            }
                            viewModel.eventFunction(AddEditEvent.ColorChange(colorInt))
                        }
                    )

                }

            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                style = MaterialTheme.typography.bodyMedium,
                text = "Recent edit is made at: $datePart"
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentDefaultTextfl(
                text = titleState.text,
                default =titleState.defaultText,
                onFocusChange ={
                    viewModel.eventFunction(AddEditEvent.ChangeTitleFocus(it))
                },
                onValChange ={
                    viewModel.eventFunction(AddEditEvent.EnteredTitle(it))
                },
                isDefaultVisible = titleState.isDefault,
                textStyle = MaterialTheme.typography.headlineMedium,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentDefaultTextfl(
                text = contentState.text,
                default =contentState.defaultText,
                onValChange ={
                    viewModel.eventFunction(AddEditEvent.EnteredContent(it))
                },
                onFocusChange ={
                    viewModel.eventFunction(AddEditEvent.ChangeContentFocus(it))
                },
                isDefaultVisible = contentState.isDefault,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxHeight()
            )

        }
    }
}
@Composable
fun ColorElement(
    isSelected: Boolean,
    color: Color,
    onColorSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .shadow(15.dp, CircleShape)
            .clip(CircleShape)
            .background(color)
            .border(
                width = 3.dp,
                color = if (isSelected) Color.DarkGray else Color.Transparent,
                shape = CircleShape
            )
            .clickable {
                onColorSelected()
            }
    )
}
@Composable
fun TransparentDefaultTextfl (
    text:String,
    default: String,
    modifier: Modifier = Modifier,
    onValChange : (String)->Unit,
    textStyle: androidx.compose.ui.text.TextStyle = androidx.compose.ui.text.TextStyle(),
    singleLine: Boolean = false,
    isDefaultVisible: Boolean = true,
    onFocusChange: (FocusState)-> Unit
){
    Box(
        modifier=modifier
    ) {
        BasicTextField(
            value = text,
            onValueChange = onValChange,
            singleLine = singleLine,
            textStyle= textStyle,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    onFocusChange(it)
                }

        )
        if(isDefaultVisible) {
            Text(
                text = default,
                style = textStyle,
                color = Color.LightGray
            )
        }

    }
}