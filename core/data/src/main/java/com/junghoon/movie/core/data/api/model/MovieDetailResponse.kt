package com.junghoon.movie.core.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailResponse(
    val id: Int,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("poster_path")
    val posterPath: String,
    val title: String,
    val tagline: String,
    @SerialName("vote_average")
    val rating: Double,
    val overview: String
) {
    companion object {
        fun createFake(): MovieDetailResponse = MovieDetailResponse(
            id = 1,
            backdropPath = "sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
            posterPath = "2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
            title = "Blue_Whale",
            tagline = "Brace yourself",
            rating = 8.5,
            overview = "Characters from different"
        )
    }
}