package com.junghoon.movie

import com.junghoon.movie.core.domain.model.MovieDetail


sealed interface DetailUiState {
    object Loading : DetailUiState
    data class Detail(val movieDetail: MovieDetail) : DetailUiState
}
