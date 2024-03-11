package com.junghoon.movie.core.data.repository

import com.junghoon.movie.core.data.api.MovieApi
import com.junghoon.movie.core.data.mapper.toData
import com.junghoon.movie.core.domain.model.Movie
import com.junghoon.movie.core.domain.model.MovieDetail
import com.junghoon.movie.core.domain.model.MovieType
import com.junghoon.movie.core.domain.model.Movies
import com.junghoon.movie.core.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
) : MovieRepository {
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

    override suspend fun getMovieDetail(id: Int): MovieDetail {
        return movieApi.getMovieDetail(id).toData()
    }

    override suspend fun getHomeMovies(movieType: MovieType): Flow<List<Movie>> {
        return flow {
            emit(movieApi.getHomeMovies(movieType.type).results.map {
                it.toData()
            })
        }
    }
}
