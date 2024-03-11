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
)