package com.infectedbyte.zerochanviewer.presentation.imageBrowseScreen.componets

import coil3.size.Dimension
import com.infectedbyte.zerochanviewer.domain.model.ZeroImage

data class BrowserState(
    val isLoading: Boolean = false,
    val images: List<ZeroImage> = emptyList(),
    val error: String = "",
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val searchParameter: Map<String, String> = mapOf("p" to "1", "l" to "200"),
    val favorite: String = "",
    val sortLatest : Boolean = true,
    val sortDimension: String = ""
)