package com.junghoon.movie.feature.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.junghoon.movie.core.domain.Constants.IMAGE_DOMAIN
import com.junghoon.movie.core.domain.model.Movie
import kotlinx.collections.immutable.persistentListOf

internal class HomeUiStatePreviewParameterProvider : PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState> = sequenceOf(
        HomeUiState.Loading,
        HomeUiState.Movies(
            nowPlaying = persistentListOf(
                Movie(
                    id = 0,
                    backdropPath = "${IMAGE_DOMAIN}sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
                    posterPath = "${IMAGE_DOMAIN}2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
                    title = "Blue_Whale"
                ),
                Movie(
                    id = 1,
                    backdropPath = "${IMAGE_DOMAIN}mOQV573Tr0WxI2VVOLKwtfaRGZH.jpg",
                    posterPath = "${IMAGE_DOMAIN}otPBQncqsboG6MyAeGY8Pur7Vac.jpg",
                    title = "The Wild"
                )
            ),
            topRated = persistentListOf(
                Movie(
                    id = 0,
                    backdropPath = "${IMAGE_DOMAIN}sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
                    posterPath = "${IMAGE_DOMAIN}2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
                    title = "Blue_Whale"
                ),
                Movie(
                    id = 1,
                    backdropPath = "${IMAGE_DOMAIN}mOQV573Tr0WxI2VVOLKwtfaRGZH.jpg",
                    posterPath = "${IMAGE_DOMAIN}otPBQncqsboG6MyAeGY8Pur7Vac.jpg",
                    title = "The Wild"
                )
            ),
            upcoming = persistentListOf(
                Movie(
                    id = 0,
                    backdropPath = "${IMAGE_DOMAIN}sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
                    posterPath = "${IMAGE_DOMAIN}2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
                    title = "Blue_Whale"
                ),
                Movie(
                    id = 1,
                    backdropPath = "${IMAGE_DOMAIN}mOQV573Tr0WxI2VVOLKwtfaRGZH.jpg",
                    posterPath = "${IMAGE_DOMAIN}otPBQncqsboG6MyAeGY8Pur7Vac.jpg",
                    title = "The Wild"
                )
            )
        ),
    )
}
