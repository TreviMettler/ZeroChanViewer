package com.infectedbyte.zerochanviewer.data.remote.dto

import com.infectedbyte.zerochanviewer.domain.model.ZeroImageDetail

data class ZeroImageDetailDto(
    val id: Int,
    val small: String,
    val medium: String,
    val large: String,
    val full: String,
    val width: Int,
    val height: Int,
    val size: Int,
    val hash: String,
    val source: String,
    val primary: String,
    val tags: List<String>
)

fun ZeroImageDetailDto.toZeroImageDetail(): ZeroImageDetail {
    return ZeroImageDetail(
        id,
        small,
        medium,
        large,
        full,
        width,
        height,
        size,
        source,
        primary,
        tags = tags.map { it }
    )
}