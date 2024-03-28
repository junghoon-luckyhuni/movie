package com.junghoon.movie.core.data.repository

import com.junghoon.movie.datastore.datastore.MoviePreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull

class FakeMoviePreferencesDataSource : MoviePreferencesDataSource {
    private val _likedMovie = MutableStateFlow(emptySet<String>())
    override val likedMovie: Flow<Set<String>> = _likedMovie.filterNotNull()

    override suspend fun updateMovieLike(likedMovie: Set<String>) {
        _likedMovie.value = likedMovie
    }
}
