package com.infectedbyte.zerochanviewer.domain.use_case.get_images

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.infectedbyte.zerochanviewer.comman.Resource
import com.infectedbyte.zerochanviewer.data.remote.dto.toZeroImage
import com.infectedbyte.zerochanviewer.domain.model.ZeroImage
import com.infectedbyte.zerochanviewer.domain.repository.ZeroImageRespository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetZeroImageUseCase @Inject constructor(
    private val repo: ZeroImageRespository
) {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun loadImages(
        searchTags: String,
        params: Map<String, String>
    ): Flow<Resource<List<ZeroImage>>> = flow {
        try {
            emit(Resource.Loading())
            val images = repo.getZeroImages(searchTags, params).map { it.toZeroImage() }
            Log.i("iBYTE", "Loaded images: ${images.size}")
            emit(Resource.Success(images))

        } catch (e: HttpException) {
            emit(Resource.Error(e.localizedMessage ?: "OOOO something fucky happened"))

        } catch (e: IOException) {
            emit(Resource.Error("Couldn't reach ZeroChan API\nCheck internet connection"))
        }
    }
}