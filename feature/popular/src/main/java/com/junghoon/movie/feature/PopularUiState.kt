package com.junghoon.movie.feature

import com.junghoon.movie.core.domain.model.Movie
import kotlinx.collections.immutable.PersistentList

data class PopularUiState(val isLoading: Boolean, val popular: PersistentList<Movie>)
