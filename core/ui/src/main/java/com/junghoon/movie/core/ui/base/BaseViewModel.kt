package com.junghoon.movie.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.SocketException
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel() {
    private val _errorFlow = MutableStateFlow<ErrorState?>(null)
    val errorFlow: StateFlow<ErrorState?> get() = _errorFlow


    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            when (throwable) {
                is SocketException, is HttpException, is UnknownHostException -> _errorFlow.emit(ErrorState.NetworkError)
                else -> _errorFlow.emit(ErrorState.ApiError(throwable.message))
            }
        }
    }

    fun ViewModel.launchWithExceptionHandler(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(exceptionHandler, block = block)
    }
}

