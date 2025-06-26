package com.infectedbyte.zerochanviewer.domain.repository

import com.infectedbyte.zerochanviewer.data.remote.dto.ZeroImageDetailDto
import com.infectedbyte.zerochanviewer.data.remote.dto.ZeroImageDto

interface ZeroImageRespository {

    suspend fun getZeroImages(tags: String, params: Map<String, String>): List<ZeroImageDto>

    suspend fun getZeroImageById(imageId: String): ZeroImageDetailDto
}