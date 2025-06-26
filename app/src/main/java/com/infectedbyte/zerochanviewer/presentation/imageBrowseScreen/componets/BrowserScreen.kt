package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets

import android.os.Build
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.infectedbyte.zerochanviewer.presentation.Screen
import com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.ImageBrowseViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun ZeroImageListScreen(
    navController: NavController,
    viewModel: ImageBrowseViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {

    val state = viewModel.state.value
    var expanded by rememberSaveable { mutableStateOf(false) }
    var privateSearchQuery by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState)
            },
        topBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                SearchBar(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(9f)
                        ,
                    expanded = expanded,
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = privateSearchQuery,
                            onQueryChange = { privateSearchQuery = it },
                            onSearch = {
                                viewModel.onEvent(BrowserEvent.Search(it))
                                expanded = false
                            },
                            expanded = expanded,
                            onExpandedChange = {
                                //TODO expanded = it
                            },
                            placeholder = { Text("Search") },
                            trailingIcon = {
                                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null)
                            }
                        )
                    },
                    onExpandedChange = {
                    //TODO expanded = it
                    }
                ) {

                }

                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                        .align(Alignment.Bottom),
                    onClick = {},
                ) {
                    Icon(Icons.Default.Settings, "Settings")
                }
            }


        }
    ) { padding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                )
            }

        }

        if (state.error.isNotBlank()) {
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

        if (!state.isLoading && state.images.isNotEmpty()) {

            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                columns = StaggeredGridCells.Fixed(2)

            ) {
                items(state.images) { image ->
                    ZeroImageItem(
                        image,
                        onClick = {
                            if (!image.tags.contains("Ecchi")) {
                                navController.navigate(Screen.ZeroImageScreenRoute(imageId = image.id.toString()))
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Can't Display Ecchi Images, Sorry")
                                }
                            }

                        }
                    )
                }

            }
        }




        if (state.images.isEmpty() && !state.isLoading && state.error.isBlank()) {
            Log.i("Ibyte", "list is empty")
//            viewModel.onEvent(BrowserEvent.Refresh)
        }

    }
}