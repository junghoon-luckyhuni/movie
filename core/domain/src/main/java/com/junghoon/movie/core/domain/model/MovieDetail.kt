package com.junghoon.movie.core.domain.model

import androidx.compose.runtime.Immutable
import com.junghoon.movie.core.domain.Constants.IMAGE_DOMAIN


@Immutable
data class MovieDetail(
    val id: Int,
    val backdropPath: String?,
    val posterPath: String,
    val title: String,
    val tagline: String,
    val rating: Double,
    val overview: String,
    val isLike: Boolean
) {
    companion object {
        fun createFake() = MovieDetail(
            id = 1,
            backdropPath = "${IMAGE_DOMAIN}sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
            posterPath = "${IMAGE_DOMAIN}2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
            title = "Blue_Whale",
            tagline = "Brace yourself",
            rating = 8.5,
            overview = "Characters from different",
            isLike = false
        )
    }
}