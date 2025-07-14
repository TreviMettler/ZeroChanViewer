package com.infectedbyte.zerochanviewer.data.remote

import com.infectedbyte.zerochanviewer.data.remote.dto.ZeroApiResponse
import com.infectedbyte.zerochanviewer.data.remote.dto.ZeroImageDetailDto
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ZeroChanApi {

    @Headers("ZeroChan-App: infectedbyte")
    @GET("/{tags}?&json")
    suspend fun getImages(
        @Path("tags") searchTags: String,
        @QueryMap params: Map<String, String>
    ): ZeroApiResponse



    @Headers("ZeroChan-App: infectedbyte")
    @GET("/{imageId}?json")
    suspend fun getImageById(@Path("imageId") imageId: String): ZeroImageDetailDto


}