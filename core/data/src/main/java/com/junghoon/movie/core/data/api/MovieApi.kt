package com.junghoon.movie.core.data.api

import com.junghoon.movie.core.data.api.model.MovieDetailResponse
import com.junghoon.movie.core.data.api.model.MoviesListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int?,
        @Query("region") region: String = "kr",
        @Query("language") language: String = "ko"
    ): MoviesListResponse

    @GET("movie/{id}")
    suspend fun getMovieDetail(@Path("id") id: Int): MovieDetailResponse

    @GET("movie/{type}")
    suspend fun getHomeMovies(
        @Path("type") type: String,
        @Query("region") region: String = "kr",
        @Query("language") language: String = "ko"
    ): MoviesListResponse
}