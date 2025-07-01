package com.infectedbyte.zerochanviewer.domain.model

data class DownloadInfo (
    val imageId: String = "",
    val imageName: String = "",
    val progress: Int = 0,
    val status: DownloadStatus = DownloadStatus.IDLE
)

enum class DownloadStatus {
    IDLE,
    IN_PROGRESS,
    COMPLETED,
    FAILED
}