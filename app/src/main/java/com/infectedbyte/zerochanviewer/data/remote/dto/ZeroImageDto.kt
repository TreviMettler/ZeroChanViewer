package com.infectedbyte.zerochanviewer.data.remote.dto

import com.infectedbyte.zerochanviewer.domain.model.ZeroImage
import kotlinx.serialization.Serializable

@Serializable
data class ZeroImageDto(
    val id: Int,
    val width: Int,
    val height: Int,
    val md5: String,
    val thumbnail: String,
    val source: String,
    val tag: String,
    val tags: List<String>
)

fun ZeroImageDto.toZeroImage(): ZeroImage {
    return ZeroImage(
        id,
        width,
        height,
        thumbnail,
        source,
        tag,
        tags = tags.map { it }
    )
}