package com.infectedbyte.zerochanviewer.presentation.imageDetail.componets

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.SubcomposeAsyncImage
import com.infectedbyte.zerochanviewer.domain.model.ZeroImageDetail
import com.infectedbyte.zerochanviewer.presentation.imageDetail.ImageDetailViewModel
import com.valentinilk.shimmer.shimmer


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
fun ZeroImageScreenPreview(modifier: Modifier = Modifier) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(true) }
    val image = ZeroImageDetail(id = 3271325, small = "https://s3.zerochan.net/240/25/26/3271325.jpg", medium = "https://s3.zerochan.net/240/25/26/3271325.jpg", large = "https://s3.zerochan.net/240/25/26/3271325.jpg", full = "https://static.zerochan.net/Ayanami.Rei.full.3271325.png", width = 800, height = 1196, size = 877568, source = null, primary = "Ayanami Rei", tags = listOf("Female", "Fanart", "Blue Hair", "Long Hair", "Red Eyes", "Short Hair", "Neon Genesis Evangelion", "Ayanami Rei", "Sitting", "Environmental Suit", "Pixiv", "Bodysuit", "Solo", "Hair Ornament", "Alternate Hairstyle", "Bent Knees", "Fanart from Pixiv", "Plugsuit (Evangelion)", "EB十"))

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            SmallFloatingActionButton(
                modifier = Modifier,
                onClick = { showBottomSheet = !showBottomSheet },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Filled.KeyboardArrowUp, "Tags")
            }
        }
    ) {  _ ->

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            SubcomposeAsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                ,
                model = image.full,
                contentDescription = image.primary,
                loading = {
                    Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize().shimmer().background(Color.Gray)
                        ) {
                            Text("")
                        }
                    }
                }
            )
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.background(MaterialTheme.colorScheme.background),
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                FlowRow(
                    modifier =  Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    image.tags.forEach { tag ->
                        ImageTag(modifier = Modifier, tag)
                    }
                }
            }
        }
    }


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZeroImageScreen(
    modifier: Modifier = Modifier,
    viewModel: ImageDetailViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            SmallFloatingActionButton(
                modifier = Modifier,
                onClick = { showBottomSheet = !showBottomSheet },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Filled.KeyboardArrowUp, "Tags")
            }
        }
    ) {
        if (!state.isLoading) {
            val image = state.image!!



            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                SubcomposeAsyncImage(
                    model = image.large,
                    contentDescription = image.primary,
                    modifier = Modifier
                        .fillMaxSize()
                        .aspectRatio(image.width.toFloat() / image.height.toFloat()),
                    loading = {
                        Column(
                            Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().shimmer().background(Color.Gray)
                            ) {
                                Text("")
                            }
                        }
                    },
                )
            }
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    Column {
                        image.source?.let {
                            Row(
                                modifier.fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                SourceTag(Modifier, image.source)
                            }
                            HorizontalDivider(modifier = Modifier.padding(4.dp))
                        }

                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            itemVerticalAlignment = Alignment.CenterVertically
                        ) {
                            image.tags.sorted().forEach { tag ->
                                ImageTag(modifier = Modifier, tag)
                            }
                        }
                    }
                }
            }
        }
    }
}
