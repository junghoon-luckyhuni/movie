package com.junghoon.movie.core.domain.model

data class Movie(
    val id: Int,
    val backdropPath: String?,
    val posterPath: String,
    val title: String
) {
    companion object {
        fun createFake() = Movie(
            id = 1,
            backdropPath = "https://image.tmdb.org/t/p/w500/sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
            posterPath = "https://image.tmdb.org/t/p/w500/2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
            title = "Blue_Whale"
        )
    }
}

data class Movies(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
)

enum class MovieType(val type: String) {
    NOW_PLAYING("now_playing"), TOP_RATED("top_rated"), UPCOMING("upcoming")
}