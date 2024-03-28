package com.junghoon.movie.datastore.datastore

import kotlinx.coroutines.flow.Flow

interface MoviePreferencesDataSource {
    val likedMovie: Flow<Set<String>>
    suspend fun updateMovieLike(likedMovie: Set<String>)
}
