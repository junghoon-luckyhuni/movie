package com.junghoon.movie.feature.home

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.junghoon.movie.core.domain.model.Movie
import kotlinx.collections.immutable.persistentListOf

internal class HomeUiStatePreviewParameterProvider : PreviewParameterProvider<HomeUiState> {
    override val values: Sequence<HomeUiState> = sequenceOf(
        HomeUiState.Loading,
        HomeUiState.Movies(
            nowPlaying = persistentListOf(
                Movie.createFake(id = 0),
                Movie.createFake(id = 1)
            ),
            topRated = persistentListOf(
                Movie.createFake(id = 0),
                Movie.createFake(id = 1)
            ),
            upcoming = persistentListOf(
                Movie.createFake(id = 0),
                Movie.createFake(id = 1)
            )
        ),
    )
}
