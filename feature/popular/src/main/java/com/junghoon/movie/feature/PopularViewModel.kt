package com.junghoon.movie.feature

import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.usecase.GetPopularMovieUseCase
import com.junghoon.movie.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PopularViewModel @Inject constructor(
    private val getPopularMovieUseCase: GetPopularMovieUseCase
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

    private fun getPopularMovies() {
        _uiState.update {
            it.copy(isLoading = true)
        }

        launchWithExceptionHandler {
            val popular = getPopularMovieUseCase.invoke()

            _uiState.update {
                it.copy(
                    isLoading = false,
                    popular = popular.toPersistentList()
                )
            }
        }
    }
}
