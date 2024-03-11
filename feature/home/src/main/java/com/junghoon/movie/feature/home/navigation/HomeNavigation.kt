package com.junghoon.movie.feature.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.junghoon.movie.core.ui.base.ErrorState
import com.junghoon.movie.feature.home.HomeRoute

fun NavController.navigateHome(navOptions: NavOptions) {
    navigate(HomeRoute.route, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
    padding: PaddingValues,
    onMovieClick: (Int) -> Unit,
    onShowErrorSnackBar: (ErrorState) -> Unit
) {
    composable(route = HomeRoute.route) {
        HomeRoute(padding, onMovieClick,  onShowErrorSnackBar)
    }
}

object HomeRoute {
    const val route = "home"
}
