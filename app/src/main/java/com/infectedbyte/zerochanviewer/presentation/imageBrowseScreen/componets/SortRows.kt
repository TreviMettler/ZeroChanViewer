package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.sp
import com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.ImageBrowseViewModel
import java.util.Locale


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SortByRecency(
    viewModel: ImageBrowseViewModel

) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Text("Sort by:")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            FilterChip(
                onClick = {
                    viewModel.onEvent(BrowserEvent.SortRecently("latest"))
                },
                label = {
                    Text("Latest")
                },
                selected = viewModel.state.value.searchParameter.containsValue("id")
            )
            FilterChip(
                onClick = {
                    viewModel.onEvent(BrowserEvent.SortRecently("popular"))
                },
                label = {
                    Text("popular")
                },
                selected = viewModel.state.value.searchParameter.containsValue("fav")
            )
        }
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun SortByDimensions(
    viewModel: ImageBrowseViewModel

) {
    Column {
        Text("Dimensions:")

        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            itemVerticalAlignment = Alignment.CenterVertically,

        ) {
            val sortDimensions = listOf("large", "huge", "landscape", "portrait", "square")

            sortDimensions.forEach { dimensions ->

                FilterChip(
                    onClick = {
                        viewModel.onEvent(BrowserEvent.SortDimensions(dimensions))
                    },
                    label = {
                        Text(dimensions.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() })
                    },
                    selected = viewModel.state.value.sortDimension.contains(dimensions)
                )
            }

        }
    }
}