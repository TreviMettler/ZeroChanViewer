package com.infectedbyte.zerochanviewer.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object BrowserScreen

    @Serializable
    data class ZeroImageScreenRoute(
        val imageId: String
    )


}