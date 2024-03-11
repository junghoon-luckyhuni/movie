package com.junghoon.movie.feature.main

import com.junghoon.movie.feature.navigation.PopularRoute
import com.junghoon.movie.feature.home.navigation.HomeRoute

internal enum class MainTab(
    val iconResId: Int,
    val labelId: Int,
    val route: String,
) {
    HOME(
        iconResId = R.drawable.ic_home,
        labelId = R.string.home,
        HomeRoute.route,
    ),
    POPULAR(
        iconResId = R.drawable.ic_popular,
        labelId = R.string.popular,
        PopularRoute.route,
    );

    companion object {
        operator fun contains(route: String): Boolean {
            return values().map { it.route }.contains(route)
        }

        fun find(route: String): MainTab? {
            return values().find { it.route == route }
        }
    }
}
