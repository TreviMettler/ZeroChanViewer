package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets

import com.infectedbyte.zerochanviewer.domain.model.ZeroImage

data class BrowserState(
    val isLoading: Boolean = false,
    val images: List<ZeroImage> = emptyList(),
    val error: String = "",
    val isNextPage: Boolean = false,
    val searchQuery: String = "",
    val searchParameter: Map<String, String> = mapOf("p" to "1", "l" to "20"),
    val isDownloadImage: Boolean = false,
    val downloadSuccess: Boolean? = null,
    val sortDimension: String = ""
)