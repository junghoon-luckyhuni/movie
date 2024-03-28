package com.junghoon.movie.feature.home

import com.junghoon.movie.core.domain.model.MovieType
import com.junghoon.movie.core.domain.usecase.GetHomeMovieUseCase
import com.junghoon.movie.core.domain.usecase.LikeMovieUseCase
import com.junghoon.movie.core.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeMovieUseCase: GetHomeMovieUseCase,
    private val likeMovieUseCase: LikeMovieUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun getHomeMovies() {
        launchWithExceptionHandler {
            combine(
                getHomeMovieUseCase(MovieType.NOW_PLAYING).catch { emit(emptyList()) },
                getHomeMovieUseCase(MovieType.TOP_RATED).catch { emit(emptyList()) },
                getHomeMovieUseCase(MovieType.UPCOMING).catch { emit(emptyList()) },
                likeMovieUseCase.getLikedMovieIds()
            ) { nowPlaying, topRated, upcoming, likedMovieIds ->
                val updatedNowPlaying = nowPlaying.map { movie ->
                    movie.copy(isLike = likedMovieIds.contains(movie.id.toString()))
                }
                val updatedTopRated = topRated.map { movie ->
                    movie.copy(isLike = likedMovieIds.contains(movie.id.toString()))
                }
                val updatedUpcoming = upcoming.map { movie ->
                    movie.copy(isLike = likedMovieIds.contains(movie.id.toString()))
                }

                HomeUiState.Movies(
                    nowPlaying = updatedNowPlaying.toPersistentList(),
                    topRated = updatedTopRated.toPersistentList(),
                    upcoming = updatedUpcoming.toPersistentList()
                )
            }.collectLatest {
                _uiState.value = it
            }
        }
    }

    fun updateLikeMovie(id: Int, isLike: Boolean) {
        launchWithExceptionHandler {
            likeMovieUseCase.updateMovieLike(id.toString(), isLike)
        }
    }
}

