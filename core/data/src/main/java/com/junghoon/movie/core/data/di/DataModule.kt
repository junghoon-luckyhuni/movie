package com.junghoon.movie.core.data.di

import com.junghoon.movie.core.data.repository.MovieRepositoryImpl
import com.junghoon.movie.core.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindsMovieRepository(
        repository: MovieRepositoryImpl,
    ): MovieRepository
}
