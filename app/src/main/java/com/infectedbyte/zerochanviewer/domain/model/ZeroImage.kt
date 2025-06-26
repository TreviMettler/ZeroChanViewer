package com.infectedbyte.zerochanviewer.domain.model

data class ZeroImage (
    val id: Int,
    val width: Int,
    val height: Int,
    val thumbnail: String,
    val source: String,
    val tag: String,
    val tags: List<String>
)