package com.junghoon.movie

import com.junghoon.movie.core.domain.usecase.GetMovieDetailUseCase
import com.junghoon.movie.core.domain.usecase.LikeMovieUseCase
import com.junghoon.movie.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val likeMovieUseCase: LikeMovieUseCase
) : BaseViewModel() {

    private val _detailUiState =
        MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val detailUiState: StateFlow<DetailUiState> = _detailUiState

    fun getMovieDetail(movieId: Int) {
        launchWithExceptionHandler {
            combine(
                getMovieDetailUseCase(movieId),
                likeMovieUseCase.getLikedMovieIds()
            ) { detail, likedMovieIds ->
                val movieDetail = detail.copy(isLike = likedMovieIds.contains(detail.id.toString()))
                DetailUiState.Detail(movieDetail)
            }.collectLatest {
                _detailUiState.value = it
            }
        }
    }

    fun updateLikeMovie(id: Int, isLike: Boolean) {
        launchWithExceptionHandler {
            likeMovieUseCase.updateMovieLike(id.toString(), isLike)
        }
    }
}
