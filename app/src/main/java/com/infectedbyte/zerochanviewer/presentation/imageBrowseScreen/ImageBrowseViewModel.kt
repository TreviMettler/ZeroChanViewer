package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.infectedbyte.zerochanviewer.comman.Resource
import com.infectedbyte.zerochanviewer.domain.model.DownloadInfo
import com.infectedbyte.zerochanviewer.domain.model.DownloadStatus
import com.infectedbyte.zerochanviewer.domain.model.ZeroImage
import com.infectedbyte.zerochanviewer.domain.use_case.DownloadImages
import com.infectedbyte.zerochanviewer.domain.use_case.get_images.GetZeroImageUseCase
import com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets.BrowserEvent
import com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets.BrowserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@HiltViewModel
class ImageBrowseViewModel @Inject constructor(
    private val getImagesUseCase: GetZeroImageUseCase,
    private val downloadImages: DownloadImages
): ViewModel() {
    private val _state = mutableStateOf(BrowserState())
    val state: State<BrowserState> = _state
    private val _downloadQueue = MutableStateFlow<List<DownloadInfo>>(emptyList())
    val downloadQueue: StateFlow<List<DownloadInfo>> = _downloadQueue.asStateFlow()


    suspend fun saveImage(imageId: String, name: String) {
        val existingDownload = _downloadQueue.value.find { it.imageId == imageId }
        if (existingDownload == null || existingDownload.status == DownloadStatus.FAILED) {
            val newDownloadInfo = DownloadInfo(imageId, name, status = DownloadStatus.IN_PROGRESS)
            _downloadQueue.update { currentList ->
                val filteredList = currentList.filterNot { it.imageId == imageId && it.status == DownloadStatus.FAILED }
                filteredList + newDownloadInfo
            }
        }

        downloadImages.downloadImage(imageId).flowOn(Dispatchers.IO).collect { result ->
            when (result) {
                is Resource.Loading -> {
                    _downloadQueue.update { currentList ->
                        currentList.map {
                            if (it.imageId == imageId && result.data != null) {
                                it.copy(progress = result.data.progress)
                            } else  it
                        }
                    }
                }
                is Resource.Success -> {
                    _downloadQueue.update { currentList ->
                        currentList.map {
                            if (it.imageId == imageId && result.data != null) {
                                it.copy(status = result.data.status)
                            } else it
                        }
                    }
                }
                is Resource.Error -> {
                    val errorMessage = result.msg
                    Log.e("DownloadImage", "Error: $errorMessage")
                }
            }
        }
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
                _state.value = _state.value.copy(searchQuery = event.query)
            }
            is BrowserEvent.Search -> {
                _state.value = _state.value.copy(searchQuery = event.query, isLoading = true)
                getZeroImages()
            }
            is BrowserEvent.SortRecently -> {
                when (event.sortType) {
                    "id" -> { _state.value = _state.value.copy(searchParameter = _state.value.searchParameter.plus("s" to "id")) }
                    "fav" -> { _state.value = _state.value.copy(searchParameter = _state.value.searchParameter.plus("s" to "fav")) }

                }
            }
            is BrowserEvent.FetchNextPage -> {
                val currentPage = _state.value.searchParameter["p"]!!.toInt()
                val nextPage = currentPage + 1
                _state.value = _state.value.copy(searchParameter = _state.value.searchParameter.plus("p" to nextPage.toString()))
                getZeroImages(_state.value.images)
            }
            is BrowserEvent.SortDimensions -> {
                val dimensions =_state.value.sortDimension
                var dimensionList = dimensions.split("|").toList()
                Log.i("iByte", dimensions)
                when (event.sortType)  {
                    "large" -> {
                        dimensionList = if (!dimensionList.contains("large")) {
                            dimensionList.plus("large")
                        } else {
                            dimensionList.minus("large")
                        }
                    }
                    "huge" -> {
                        dimensionList = if (!dimensionList.contains("huge")) {
                            dimensionList.plus("huge")
                        } else {
                            dimensionList.minus("huge")
                        }
                    }
                    "landscape" -> {
                        dimensionList = if (!dimensionList.contains("landscape")) {
                            dimensionList.plus("landscape")
                        } else {
                            dimensionList.minus("landscape")
                        }
                    }
                    "portrait" -> {
                        dimensionList = if (!dimensionList.contains("portrait")) {
                            dimensionList.plus("portrait")
                        } else {
                            dimensionList.minus("portrait")
                        }
                    }
                    "square" -> {
                        dimensionList = if (!dimensionList.contains("square")) {
                            dimensionList.plus("square")
                        } else {
                            dimensionList.minus("square")
                        }
                    }
                }
                val newDimensionList = dimensionList.toString().replace(", ", "|").removeSurrounding("[", "]")
                _state.value = _state.value.copy(sortDimension = newDimensionList, searchParameter = mapOf("d" to dimensionList.toString().removeSurrounding("[", "]")))


            }
            is BrowserEvent.RemoveDownloadItem -> {
                _downloadQueue.update { currentList ->
                    currentList.filterNot { downloadInfo ->
                        downloadInfo.imageId == event.id
                    }
                }
            }

        }
    }

    private fun getZeroImages(
        imageList: List<ZeroImage> = emptyList()
    ) {
        getImagesUseCase.loadImages(
            searchTags = _state.value.searchQuery,
            params = _state.value.searchParameter
        ) .onEach { result ->
            when(result) {
                is Resource.Error -> {
                    _state.value = BrowserState(error = result.msg ?: "I don't know what happened :P")
                }
                is Resource.Loading -> {
                    if (_state.value.images.isEmpty()) {
                        _state.value = _state.value.copy(isLoading = true)
                    }else {
                        _state.value = _state.value.copy(isNextPage = true)
                    }
                }
                is Resource.Success -> {
                    _state.value = _state.value.copy(images = imageList.plus(result.data?: emptyList()) , isLoading = false, isNextPage = false)

                }
            }
        }.launchIn(viewModelScope)
    }
}
