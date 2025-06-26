package com.infectedbyte.zerochanviewer.domain.use_case.get_image

import android.util.Log
import com.infectedbyte.zerochanviewer.comman.Resource
import com.infectedbyte.zerochanviewer.data.remote.dto.toZeroImageDetail
import com.infectedbyte.zerochanviewer.domain.model.ZeroImageDetail
import com.infectedbyte.zerochanviewer.domain.repository.ZeroImageRespository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetZeroImageDetailUseCase @Inject constructor(
    private val repo: ZeroImageRespository
) {
    operator fun invoke(imageId: String): Flow<Resource<ZeroImageDetail>> = flow {
        try {
            emit(Resource.Loading())
            Log.d("iByte", "Fetching image $imageId")
            val image = repo.getZeroImageById(imageId)
            Log.i("iBYTE", "image size: ${image.size}")
            emit(Resource.Success(image.toZeroImageDetail()))

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "OOOO something fucky happened"))

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach ZeroChan API, Check internet connection"))
        }
    }
}