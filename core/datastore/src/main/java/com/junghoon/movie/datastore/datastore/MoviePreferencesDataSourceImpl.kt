package com.junghoon.movie.datastore.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviePreferencesDataSourceImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : MoviePreferencesDataSource {
    object PreferencesKey {
        val LIKED_MOVIE = stringSetPreferencesKey("LIKED_MOVIE")
    }

    override val likedMovie = dataStore.data.map { preferences ->
        preferences[PreferencesKey.LIKED_MOVIE] ?: emptySet()
    }

    override suspend fun updateMovieLike(likedMovie: Set<String>) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.LIKED_MOVIE] = likedMovie
        }
    }
}
