package com.junghoon.movie.core.domain.model

import androidx.compose.runtime.Immutable
import com.junghoon.movie.core.domain.Constants.IMAGE_DOMAIN


@Immutable
data class Movie(
    val id: Int,
    val backdropPath: String?,
    val posterPath: String,
    val title: String
) {
    companion object {
        fun createFake(id: Int = 1) = Movie(
            id = id,
            backdropPath = "${IMAGE_DOMAIN}sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
            posterPath = "${IMAGE_DOMAIN}2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
            title = "Blue_Whale"
        )
    }
}

@Immutable
data class Movies(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)

enum class MovieType(val type: String) {
    NOW_PLAYING("now_playing"), TOP_RATED("top_rated"), UPCOMING("upcoming")
}