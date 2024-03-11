package com.junghoon.movie.feature.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.junghoon.movie.core.ui.base.ErrorState
import com.junghoon.movie.feature.PopularRoute

fun NavController.navigatePopular(navOptions: NavOptions) {
    navigate(PopularRoute.route, navOptions)
}

fun NavGraphBuilder.popularNavGraph(
    padding: PaddingValues,
    onShowErrorSnackBar: (ErrorState) -> Unit,
    onMovieClick: (Int) -> Unit
) {
    composable(route = PopularRoute.route) {
        PopularRoute(
            padding = padding,
            onShowErrorSnackBar = onShowErrorSnackBar,
            onMovieClick = onMovieClick
        )
    }
}

object PopularRoute {
    const val route = "popular"
}
