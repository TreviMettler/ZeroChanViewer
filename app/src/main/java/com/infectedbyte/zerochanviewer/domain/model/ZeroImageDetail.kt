package com.infectedbyte.zerochanviewer.domain.model

data class ZeroImageDetail(
    val id: Int,
    val small: String,
    val medium: String,
    val large: String,
    val full: String,
    val width: Int,
    val height: Int,
    val size: Int,
    val source: String?,
    val primary: String,
    val tags: List<String>
)
