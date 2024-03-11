package com.junghoon.movie.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.junghoon.movie.core.domain.model.MovieType
import com.junghoon.movie.core.domain.usecase.GetHomeMovieUseCase
import com.junghoon.movie.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeMovieUseCase: GetHomeMovieUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getHomeMovies() {
        launchWithExceptionHandler {
            combine(
                getHomeMovieUseCase(MovieType.NOW_PLAYING).catch { emit(emptyList()) },
                getHomeMovieUseCase(MovieType.TOP_RATED).catch { emit(emptyList()) },
                getHomeMovieUseCase(MovieType.UPCOMING).catch { emit(emptyList()) }
            ) { nowPlaying, topRated, upcoming ->
                HomeUiState.Movies(
                    nowPlaying = nowPlaying.toPersistentList(),
                    topRated = topRated.toPersistentList(),
                    upcoming = upcoming.toPersistentList()
                )
            }.collectLatest {
                _uiState.value = it
            }
        }
    }
}
