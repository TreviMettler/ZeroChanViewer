package com.infectedbyte.zerochanviewer.di

import com.infectedbyte.zerochanviewer.comman.Constants
import com.infectedbyte.zerochanviewer.data.remote.ZeroChanApi
import com.infectedbyte.zerochanviewer.data.repository.ZeroImagesRepositoryImpl
import com.infectedbyte.zerochanviewer.domain.repository.ZeroImageRespository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideZeroChanApi(): ZeroChanApi {
        // URL logging
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        val client = OkHttpClient.Builder()
            . addInterceptor(interceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ZeroChanApi::class.java)
    }

    @Provides
    @Singleton
    fun provideZeroImageRepository(api: ZeroChanApi): ZeroImageRespository {
        return ZeroImagesRepositoryImpl(api)
    }
}