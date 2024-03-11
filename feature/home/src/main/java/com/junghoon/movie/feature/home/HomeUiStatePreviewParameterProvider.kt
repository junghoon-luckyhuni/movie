package com.junghoon.movie.feature.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.junghoon.movie.core.domain.model.Movie
import kotlinx.collections.immutable.persistentListOf

internal class HomeUiStatePreviewParameterProvider : PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState> = sequenceOf(
        HomeUiState.Loading,
        HomeUiState.Movies(
            nowPlaying = persistentListOf(
                Movie(
                    id = 0,
                    backdropPath = "https://image.tmdb.org/t/p/w500/sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
                    posterPath = "https://image.tmdb.org/t/p/w500/2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
                    title = "Blue_Whale"
                ),
                Movie(
                    id = 1,
                    backdropPath = "https://image.tmdb.org/t/p/w500/mOQV573Tr0WxI2VVOLKwtfaRGZH.jpg",
                    posterPath = "https://image.tmdb.org/t/p/w500/otPBQncqsboG6MyAeGY8Pur7Vac.jpg",
                    title = "The Wild"
                )
            ),
            topRated = persistentListOf(
                Movie(
                    id = 0,
                    backdropPath = "https://image.tmdb.org/t/p/w500/sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
                    posterPath = "https://image.tmdb.org/t/p/w500/2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
                    title = "Blue_Whale"
                ),
                Movie(
                    id = 1,
                    backdropPath = "https://image.tmdb.org/t/p/w500/mOQV573Tr0WxI2VVOLKwtfaRGZH.jpg",
                    posterPath = "https://image.tmdb.org/t/p/w500/otPBQncqsboG6MyAeGY8Pur7Vac.jpg",
                    title = "The Wild"
                )
            ),
            upcoming = persistentListOf(
                Movie(
                    id = 0,
                    backdropPath = "https://image.tmdb.org/t/p/w500/sKdDC38FHuPvEsRgIzMEQE6ydol.jpg",
                    posterPath = "https://image.tmdb.org/t/p/w500/2TmZ2lgDEYJAFpeUMALRx5NwqK.jpg",
                    title = "Blue_Whale"
                ),
                Movie(
                    id = 1,
                    backdropPath = "https://image.tmdb.org/t/p/w500/mOQV573Tr0WxI2VVOLKwtfaRGZH.jpg",
                    posterPath = "https://image.tmdb.org/t/p/w500/otPBQncqsboG6MyAeGY8Pur7Vac.jpg",
                    title = "The Wild"
                )
            )
        ),
    )
}
