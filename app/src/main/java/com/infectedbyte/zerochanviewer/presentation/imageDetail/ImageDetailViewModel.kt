package com.infectedbyte.zerochanviewer.presentation.imageDetail

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.infectedbyte.zerochanviewer.comman.Resource
import com.infectedbyte.zerochanviewer.domain.use_case.get_image.GetZeroImageDetailUseCase
import com.infectedbyte.zerochanviewer.presentation.Screen
import com.infectedbyte.zerochanviewer.presentation.imageDetail.componets.ImageState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(
    private val getZeroImageDetailUseCase: GetZeroImageDetailUseCase,
    savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _state = mutableStateOf(ImageState())
    val state: State<ImageState> = _state

    init {
        val imageId = savedStateHandle.toRoute<Screen.ZeroImageScreenRoute>()
        getZeroImage(imageId.imageId)

    }

    fun openBrowser(url: String) {
        _state.value = _state.value.copy(intentError = null)
        val browserIntent = Intent(Intent.ACTION_VIEW, url.toUri())
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(browserIntent)
        } catch (e: ActivityNotFoundException) {
            Log.i("IBYTE", "no activity found")
            _state.value = _state.value.copy(intentError = e.toString())
        }
    }

    private fun getZeroImage(imageId: String) {
        getZeroImageDetailUseCase(imageId).onEach { result ->
            when(result) {
                is Resource.Error -> {
                    _state.value = ImageState(error = result.msg ?: "I don't know what happened :P")
                }
                is Resource.Loading -> {
                    _state.value = ImageState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = ImageState(image = result.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}