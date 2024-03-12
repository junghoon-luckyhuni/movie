package com.junghoon.movie.core.data.mapper

import com.junghoon.movie.core.data.api.model.MovieDetailResponse
import com.junghoon.movie.core.data.api.model.MovieResponse
import com.junghoon.movie.core.domain.Constants.IMAGE_DOMAIN
import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.model.MovieDetail

internal fun MovieResponse.toData(): Movie =
    Movie(
        id = id,
        backdropPath = "${IMAGE_DOMAIN}${this.backdropPath}",
        posterPath = "${IMAGE_DOMAIN}${this.posterPath}",
        title = title
    )


internal fun MovieDetailResponse.toData(): MovieDetail =
    MovieDetail(
        id = id,
        backdropPath = "${IMAGE_DOMAIN}${this.backdropPath}",
        posterPath = "${IMAGE_DOMAIN}${this.posterPath}",
        title = title,
        tagline = tagline,
        rating = rating,
        overview = overview
    )