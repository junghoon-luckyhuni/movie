package com.junghoon.movie.feature.home

import com.junghoon.movie.core.domain.model.Movie
import kotlinx.collections.immutable.PersistentList

sealed interface HomeUiState {
    object Loading : HomeUiState

    object Empty : HomeUiState

    data class Movies(
        val nowPlaying: PersistentList<Movie>,
        val topRated: PersistentList<Movie>,
        val upcoming: PersistentList<Movie>
    ) : HomeUiState
}
