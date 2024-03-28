package com.junghoon.movie.core.domain.usecase

import com.junghoon.movie.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LikeMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository,
) {

    suspend fun updateMovieLike(id: String, isLike: Boolean) {
        movieRepository.updateMovieLike(id, isLike)
    }

    fun getLikedMovieIds(): Flow<Set<String>> {
        return movieRepository.getLikedMovieIds()
    }
}