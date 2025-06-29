package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infectedbyte.zerochanviewer.comman.Resource
import com.infectedbyte.zerochanviewer.domain.use_case.DownloadImages
import com.infectedbyte.zerochanviewer.domain.use_case.get_images.GetZeroImageUseCase
import com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets.BrowserEvent
import com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets.BrowserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class ImageBrowseViewModel @Inject constructor(
    private val getImagesUseCase: GetZeroImageUseCase,
    private val downloadImages: DownloadImages
): ViewModel() {
    private val _state = mutableStateOf(BrowserState())
    val state: State<BrowserState> = _state


    suspend fun saveImage(imageId: String) {
        downloadImages.downloadImage(imageId)
    }

    init {
        getZeroImages()
    }

    fun onEvent(event: BrowserEvent) {
        when(event) {
            is BrowserEvent.Refresh -> {
                getZeroImages()
            }
            is BrowserEvent.SearchQueryChange -> {
                _state.value = BrowserState(searchQuery = event.query)
            }
            is BrowserEvent.Search -> {
                _state.value = BrowserState(searchQuery = event.query, isLoading = true)
                getZeroImages()
            }
            is BrowserEvent.SortRecently -> {
                when (event.sortType) {
                    "latest" -> { _state.value = BrowserState(searchParameter = mapOf("s" to "id")) }
                    "popular" -> { _state.value = BrowserState(searchParameter = mapOf("s" to "fav")) }

                }
            }
            is BrowserEvent.SortColor -> {

            }
            is BrowserEvent.SortDimensions -> {
                when (event.sortType)  {
                    "" -> {}
                    "large" -> { _state.value = BrowserState(searchParameter = mapOf("d" to "large"), sortDimension = "large") }
                    "huge" -> { _state.value = BrowserState(searchParameter = mapOf("d" to "huge"), sortDimension = "huge") }
                    "landscape" -> {_state.value = BrowserState(searchParameter = mapOf("d" to "landscape"), sortDimension = "landscape") }
                    "portrait" -> { _state.value = BrowserState(searchParameter = mapOf("d" to "portrait"), sortDimension = "portrait") }
                    "square" -> { _state.value = BrowserState(searchParameter = mapOf("d" to "square"), sortDimension = "square") }
                }
            }
        }
    }

    private fun getZeroImages() {
        getImagesUseCase(
            searchTags = _state.value.searchQuery,
            params = _state.value.searchParameter
        ).onEach { result ->
            when(result) {
                is Resource.Error -> {
                    _state.value = BrowserState(error = result.msg ?: "I don't know what happened :P")
                }
                is Resource.Loading -> {
                    _state.value = BrowserState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = BrowserState(images = result.data ?: emptyList())

                }
            }
        }.launchIn(viewModelScope)
    }
}
