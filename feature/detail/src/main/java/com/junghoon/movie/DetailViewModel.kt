package com.junghoon.movie

import com.junghoon.movie.core.domain.usecase.GetMovieDetailUseCase
import com.junghoon.movie.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
) : BaseViewModel() {

    private val _detailUiState =
        MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState

    fun getMovieDetail(movieId: Int) {
        launchWithExceptionHandler {
            val movieDetail = getMovieDetailUseCase(movieId)
            _detailUiState.value = DetailUiState.Detail(movieDetail)
        }
    }
}
