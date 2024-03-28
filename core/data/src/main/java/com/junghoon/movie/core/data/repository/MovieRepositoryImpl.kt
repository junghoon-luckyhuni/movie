package com.junghoon.movie.core.data.repository

import com.junghoon.movie.core.data.api.MovieApi
import com.junghoon.movie.core.data.mapper.toData
import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.model.MovieDetail
import com.junghoon.movie.core.domain.model.MovieType
import com.junghoon.movie.core.domain.model.Movies
import com.junghoon.movie.core.domain.repository.MovieRepository
import com.junghoon.movie.datastore.datastore.MoviePreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDataSource: MoviePreferencesDataSource
) : MovieRepository {

    private val likedMovieIds: Flow<Set<String>> = movieDataSource.likedMovie

    override suspend fun getPopularMovies(page: Int?): Movies {
        val response = movieApi.getPopularMovies(page)

        return Movies(
            page = response.page,
            results = response.results.map {
                it.toData()
            },
            totalPages = response.totalPages,
            totalResults = response.totalResults
        )
    }

    override fun getMovieDetail(id: Int): Flow<MovieDetail> {
        return flow {
            emit(movieApi.getMovieDetail(id).toData())
        }
    }

    override fun getHomeMovies(movieType: MovieType): Flow<List<Movie>> {
        return flow {
            emit(movieApi.getHomeMovies(movieType.type).results.map {
                it.toData()
            })
        }
    }

    override fun getLikedMovieIds(): Flow<Set<String>> {
        return likedMovieIds.filterNotNull()
    }

    override suspend fun updateMovieLike(id: String, isLike: Boolean) {
        val currentLikedMovieIds = likedMovieIds.first()
        movieDataSource.updateMovieLike(
            if (isLike) {
                currentLikedMovieIds + id
            } else {
                currentLikedMovieIds - id
            }
        )
    }
}
