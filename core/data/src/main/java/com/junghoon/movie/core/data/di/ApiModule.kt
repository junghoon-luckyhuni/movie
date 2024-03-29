package com.junghoon.movie.core.data.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.junghoon.movie.core.data.BuildConfig
import com.junghoon.movie.core.data.api.MovieApi
import com.junghoon.movie.core.data.api.intercepter.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(ApiKeyInterceptor())

        return client.build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(
        json: Json,
    ): Converter.Factory {
        return json.asConverterFactory("application/json".toMediaType())
    }

    @Provides
    @Singleton
    fun provideMovieApi(
        okHttpClient: OkHttpClient,
        converterFactory: Converter.Factory,
    ): MovieApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_DOMAIN)
            .addConverterFactory(converterFactory)
            .client(okHttpClient).build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }
}
