package com.junghoon.movie.feature

import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.usecase.GetPopularMovieUseCase
import com.junghoon.movie.core.domain.usecase.LikeMovieUseCase
import com.junghoon.movie.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val getPopularMovieUseCase: GetPopularMovieUseCase,
    private val likeMovieUseCase: LikeMovieUseCase
) : BaseViewModel() {
    private val _uiState =
        MutableStateFlow(
            PopularUiState(
                isLoading = true,
                popular = emptyList<Movie>().toPersistentList()
            )
        )
    val uiState: StateFlow<PopularUiState> = _uiState.asStateFlow()

    init {
        getPopularMovies()
    }

    fun loadMorePopular() {
        if (!_uiState.value.isLoading && getPopularMovieUseCase.canPaginate) {
            getPopularMovies()
        }
    }

    fun pullToRefresh() {
        getPopularMovies(true)
    }

    private fun getPopularMovies(pullToRefresh: Boolean = false) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        launchWithExceptionHandler {
            combine(
                getPopularMovieUseCase.invoke(pullToRefresh),
                likeMovieUseCase.getLikedMovieIds()
            ) { newPopular, likedMovieIds ->

                val allPopularMovies = if (pullToRefresh) {
                    newPopular
                } else {
                    (_uiState.value.popular + newPopular)
                }

                allPopularMovies.map { movie ->
                    movie.copy(isLike = likedMovieIds.contains(movie.id.toString()))
                }
            }.collectLatest { allPopularMovies ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        popular = allPopularMovies.toPersistentList()
                    )
                }
            }
        }
    }

    fun updateLikeMovie(id: Int, isLike: Boolean) {
        launchWithExceptionHandler {
            likeMovieUseCase.updateMovieLike(id.toString(), isLike)
        }
    }
}
