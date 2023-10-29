package com.velkonost.getbetter.android.features.diary.notes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.velkonost.getbetter.android.features.diary.notes.components.AddNoteItem
import com.velkonost.getbetter.android.features.diary.notes.item.NoteItem
import com.velkonost.getbetter.core.compose.components.Loader
import com.velkonost.getbetter.core.compose.extensions.fadingEdge
import com.velkonost.getbetter.shared.features.notes.api.model.Note

@Composable
fun NotesView(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    items: List<Note>,
    createGoalClick: () -> Unit,
    createNoteClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (isLoading) {
            Loader(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .fadingEdge(),
                contentPadding = PaddingValues(bottom = 140.dp)
            ) {
                items(items) { item ->
                    NoteItem(item = item)
                }

            }

            AddNoteItem(
                createGoalClick = createGoalClick,
                createNoteClick = createNoteClick
            )
        }
    }
}