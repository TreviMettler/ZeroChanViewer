package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.ImageBrowseViewModel


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SortByRecency(
    viewModel: ImageBrowseViewModel

) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Sort by:")

        FilterChip(
            onClick = {
                viewModel.onEvent(BrowserEvent.SortRecently("latest"))
            },
            label = {
                Text("Latest")
            },
            selected = viewModel.state.value.sortLatest
        )
        FilterChip(
            onClick = {
                viewModel.onEvent(BrowserEvent.SortRecently("popular"))
            },
            label = {
                Text("popular")
            },
            selected = !viewModel.state.value.sortLatest
        )
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SortByDimensions(
    viewModel: ImageBrowseViewModel

) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Dimensions:")

        val sortDimensions = listOf("large", "huge", "landscape", "portrait", "square")

        sortDimensions.forEach { dimensions ->

            FilterChip(
                onClick = {
                    viewModel.onEvent(BrowserEvent.SortDimensions(dimensions))
                },
                label = {
                    Text(dimensions)
                },
                selected = viewModel.state.value.sortDimension == dimensions
            )
        }
        OutlinedButton(
            onClick = {
                viewModel.onEvent(BrowserEvent.SortDimensions(""))
            }
        ) {
            Text("Clear")
        }

    }
}