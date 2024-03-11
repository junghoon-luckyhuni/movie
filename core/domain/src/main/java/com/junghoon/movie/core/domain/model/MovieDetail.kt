package com.junghoon.movie.core.domain.model

import androidx.compose.runtime.Immutable


@Immutable
data class MovieDetail(
    val id: Int,
    val backdropPath: String?,
    val posterPath: String,
    val title: String,
    val tagline: String,
    val rating: Double,
    val overview: String,
) {
    companion object {
        fun createFake() = MovieDetail(
            id = 1,
            backdropPath = "https://image.tmdb.org/t/p/w500/sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
            posterPath = "https://image.tmdb.org/t/p/w500/2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
            title = "Blue_Whale",
            tagline = "Brace yourself",
            rating = 8.5,
            overview = "Characters from different"
        )
    }
}