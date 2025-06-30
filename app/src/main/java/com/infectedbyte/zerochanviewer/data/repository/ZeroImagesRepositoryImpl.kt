package com.infectedbyte.zerochanviewer.data.repository

import com.infectedbyte.zerochanviewer.data.remote.ZeroChanApi
import com.infectedbyte.zerochanviewer.data.remote.dto.ZeroImageDetailDto
import com.infectedbyte.zerochanviewer.data.remote.dto.ZeroImageDto
import com.infectedbyte.zerochanviewer.domain.repository.ZeroImageRespository
import javax.inject.Inject

class ZeroImagesRepositoryImpl @Inject constructor(
    private val api: ZeroChanApi
): ZeroImageRespository {
    override suspend fun getZeroImages(tags: String, params: Map<String, String>): List<ZeroImageDto> {
        val items = api.getImages(
            tags,
            params
        ).items

        return items.map { ZeroImageDto(
            id = it.id,
            width = it.width,
            height = it.height,
            md5 = it.md5,
            thumbnail = it.thumbnail,
            source = it.source,
            tag = it.tag,
            tags = it.tags,
        ) }
    }

    override suspend fun getZeroImageById(imageId: String): ZeroImageDetailDto {
        return api.getImageById(imageId)

    }
}