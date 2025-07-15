package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.infectedbyte.zerochanviewer.R
import com.infectedbyte.zerochanviewer.domain.model.DownloadStatus
import com.infectedbyte.zerochanviewer.presentation.Screen
import com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.ImageBrowseViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ZeroImageListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ImageBrowseViewModel = hiltViewModel(),

) {
    val scrollState = rememberLazyStaggeredGridState()
    val state = viewModel.state.value
    val images = state.images
    var privateSearchQuery by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    var openDownloadMenu by remember { mutableStateOf(false) }
    val downloads by viewModel.downloadQueue.collectAsState()

    val shouldLoadMore by remember { derivedStateOf {
        val itemCount = scrollState.layoutInfo.totalItemsCount
        val lastVisibleItemIndex = scrollState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
        itemCount > 0 && lastVisibleItemIndex >= itemCount - 10

    } }

    LaunchedEffect(shouldLoadMore) {
        if (!state.isLoading && state.error.isBlank()) viewModel.onEvent(BrowserEvent.FetchNextPage)
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp, bottom = 12.dp, top = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SearchBar(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(9f),
                    expanded = false,
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = privateSearchQuery,
                            onQueryChange = { privateSearchQuery = it },
                            onSearch = {
                                viewModel.onEvent(BrowserEvent.Search(it))
                            },
                            expanded = false,
                            onExpandedChange = {
                            },
                            placeholder = { Text("Search") }
                        )
                    },
                    onExpandedChange = {
                    }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                    }
                }
            }

        },
        floatingActionButtonPosition = FabPosition.Start,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier,
                onClick = {
                    openDownloadMenu = true
                }
            ) {
                if (downloads.isEmpty()) Icon(painterResource(R.drawable.download_arrow_cool), contentDescription = null)
                if (downloads.any { it.status == DownloadStatus.IN_PROGRESS } && downloads.isNotEmpty()) Icon(painterResource(R.drawable.downloading), contentDescription = null)
                if (downloads.all { it.status == DownloadStatus.COMPLETED } && downloads.isNotEmpty()) Icon(painterResource(R.drawable.download_done), contentDescription = null)
                if (downloads.all { it.status == DownloadStatus.FAILED } && downloads.isNotEmpty()) Icon(Icons.Default.Close, contentDescription = null, tint = Color.Red)


                DropdownMenu(
                    expanded = openDownloadMenu,
                    onDismissRequest = {
                        openDownloadMenu = false
                    }
                ) {
                    if (downloads.isEmpty()) {
                        DropdownMenuItem(
                            text = {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("Just some crickets here")
                                    Text("(You can click items to remove them)", color = Color.Gray, fontSize = 12.sp)
                                }
                                   },
                            onClick = {}
                        )
                    } else {
                        downloads.forEach { downloadInfo ->
                            DownloadMenu(modifier, downloadInfo, viewModel)
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (state.error.isNotBlank() && images.isEmpty()&& !state.isLoading) {
            Column(Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = state.error,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
                Button(onClick = {
                    viewModel.onEvent((BrowserEvent.Search(privateSearchQuery)))
                }) { Text("Refresh")}
            }
        }


        if (state.isLoading && state.error.isBlank()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        LazyVerticalStaggeredGrid(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            columns = StaggeredGridCells.Fixed(2)
        ) {

            if (!state.isLoading && images.isNotEmpty()) {
                items(images, key = {image -> image.id}) { image ->
                    if (!image.tags.contains("Ecchi")) {
                        ZeroImageItem(
                            zeroImage = image,
                            onClick = {
                                navController.navigate(Screen.ZeroImageScreenRoute(imageId = image.id.toString()))
                            },
                            onDownloadClick = {
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = "Downloading ${image.tag}",
                                        duration = SnackbarDuration.Short
                                    )
                                    viewModel.saveImage(image.id.toString(), image.tag)
                                }
                            }
                        )
                    }
                }
            }
            if (state.isNextPage) {
                item{
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {

                        CircularProgressIndicator()
                    }
                }
            }
        }


    }
}

