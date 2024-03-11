package com.junghoon.movie.core.ui.base

sealed interface ErrorState {
    object NetworkError : ErrorState
    data class ApiError(val errorMessage: String?) : ErrorState
}
