package com.infectedbyte.zerochanviewer.domain.use_case

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.infectedbyte.zerochanviewer.domain.repository.ZeroImageRespository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.net.URL
import javax.inject.Inject


class DownloadImages @Inject constructor(
    private val repo: ZeroImageRespository,
    @ApplicationContext private val context: Context
) {

    suspend fun downloadImage(imageId: String) {
        return withContext(Dispatchers.IO) {
            try {

                val resolver = context.contentResolver

                val imageData = repo.getZeroImageById(imageId)
                Log.i("iByte", "image ${imageData.id} downloading")
                val url = URL(imageData.full)
                val connection = url.openConnection()
                connection.connect()
                val inputStream = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()


                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "${imageData.primary}_${imageData.id}.png")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/ZeroChan")
                    put(MediaStore.Images.Media.DESCRIPTION, imageData.primary)
                    put(MediaStore.Images.Media.IS_PENDING, 1)

                }

                var imageUri: Uri? = null
                var outputStream: OutputStream? = null

                try {

                    imageUri =
                        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    imageUri?.let { uri ->
                        outputStream = resolver.openOutputStream(uri)
                        outputStream?.let { out ->
                            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                                // Handle compression error
                                resolver.delete(uri, null, null) // Clean up if saving failed
                                imageUri = null
                            }
                        }
                    }
                } catch (e: Exception) {
                    imageUri?.let { resolver.delete(it, null, null) } // Clean up on error
                    imageUri = null
                    // Log or handle exception
                } finally {
                    Log.d("iByte", "image ${imageData.id} downloaded")
                    outputStream?.close()
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && imageUri != null) {
                    contentValues.clear()
                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(imageUri!!, contentValues, null, null)
                }
            // Return the URI of the saved image, or null if failed

            } catch (e: Exception) {
               // Log or handle download exception
           }

        }
    }
}