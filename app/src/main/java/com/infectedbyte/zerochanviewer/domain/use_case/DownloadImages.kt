package com.infectedbyte.zerochanviewer.domain.use_case

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.infectedbyte.zerochanviewer.comman.Resource
import com.infectedbyte.zerochanviewer.domain.model.DownloadInfo
import com.infectedbyte.zerochanviewer.domain.model.DownloadStatus
import com.infectedbyte.zerochanviewer.domain.repository.ZeroImageRespository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.net.URL
import javax.inject.Inject


class DownloadImages @Inject constructor(
    private val repo: ZeroImageRespository,
    @ApplicationContext private val context: Context
) {

     fun downloadImage(imageId: String): Flow<Resource<DownloadInfo>>  = flow{
        emit(Resource.Loading())
            try {

                val resolver = context.contentResolver

                val imageData = repo.getZeroImageById(imageId)
                Log.i("iByte", "image ${imageData.id} downloading")
                val url = URL(imageData.full)
                val connection = url.openConnection()
                connection.connect()
                connection.connectTimeout = 0

                val totalBytes = imageData.size
                val inputStream = connection.getInputStream()
                val outputStreamByteArray = ByteArrayOutputStream()

                var bytesRead = 0
                val buffer = ByteArray(1024)
                var len: Int


                while (inputStream.read(buffer).also { len = it } != -1) {
                    outputStreamByteArray.write(buffer, 0 , len)
                    bytesRead += len
                    if (totalBytes > 0) { // Check if totalBytes is available for progress calculation
                        val progress = (bytesRead * 100 / totalBytes)
                        emit(Resource.Loading(DownloadInfo(progress = progress))) // Emit progress
                    } else {
                        // If totalBytes is not available, you might emit bytesRead or a generic loading state
                        emit(Resource.Loading(DownloadInfo(progress = bytesRead))) // Or some other indicator
                    }
                }
                val imageBytes = outputStreamByteArray.toByteArray()
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0 , imageBytes.size)
                inputStream.close()
                outputStreamByteArray.close()


                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "${imageData.primary}_${imageData.id}.png")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ZeroChan")
                    put(MediaStore.Images.Media.DESCRIPTION, imageData.tags.toString())
                    put(MediaStore.Images.Media.IS_PENDING, 1)

                }

                var imageUri: Uri? = null
                var outputStream: OutputStream? = null

                try {

                    imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                    imageUri?.let { uri ->
                        outputStream = resolver.openOutputStream(uri, "w")
                        outputStream?.let { out ->
                            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                                resolver.delete(uri, null, null)
                                imageUri = null
                                emit(Resource.Error("Failed to save image"))
                                return@flow
                            }
                        }
                    }
                } catch (e: Exception) {
                    imageUri?.let { resolver.delete(it, null, null) }
                    imageUri = null
                    emit(Resource.Error("Something went wrong"))
                } finally {
                    Log.d("iByte", "image ${imageData.id} downloaded")
                }

                if (imageUri != null) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(imageUri!!, contentValues, null, null)
                }
                outputStream?.close()

                emit(Resource.Success(DownloadInfo(status = DownloadStatus.COMPLETED)))

            // Return the URI of the saved image, or null if failed

            } catch (e: Exception) {
                emit(Resource.Error("Couldn't save image "))
           }
    }
}