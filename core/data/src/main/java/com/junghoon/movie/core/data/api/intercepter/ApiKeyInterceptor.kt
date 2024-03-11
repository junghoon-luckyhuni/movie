package com.junghoon.movie.core.data.api.intercepter

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url =
            request.url.newBuilder().addQueryParameter("api_key", "515cc35a4a5a0d8846cea73d1de167bf")
                .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}