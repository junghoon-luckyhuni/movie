package com.junghoon.movie.core.data.api.intercepter

import com.junghoon.movie.core.data.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url =
            request.url.newBuilder().addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}