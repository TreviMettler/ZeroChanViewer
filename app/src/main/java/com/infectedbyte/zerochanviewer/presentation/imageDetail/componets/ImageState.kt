package com.infectedbyte.zerochanviewer.presentation.imageDetail.componets

import com.infectedbyte.zerochanviewer.domain.model.ZeroImageDetail

data class ImageState(
    val isLoading: Boolean = false,
    val image: ZeroImageDetail? = null,
    val error: String = ""
)