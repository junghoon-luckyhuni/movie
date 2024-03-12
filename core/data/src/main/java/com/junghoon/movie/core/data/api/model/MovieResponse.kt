package com.junghoon.movie.core.data.api.model

import com.junghoon.movie.core.domain.Constants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieResponse(
    val id: Int,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("poster_path")
    val posterPath: String,
    val title: String
) {
    companion object {
        fun createFake(id: Int = 1) = MovieResponse(
            id = id,
            backdropPath = "sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
            posterPath = "2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
            title = "Blue_Whale"
        )
    }
}

@Serializable
data class MoviesListResponse(
    val page: Int,
    val results: List<MovieResponse>,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("total_results")
    val totalResults: Int
) {
    companion object {
        fun createFake() = MoviesListResponse(
            page = 1,
            results = listOf(MovieResponse.createFake()),
            totalPages = 3,
            totalResults = 3
        )
    }
}