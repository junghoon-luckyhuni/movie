package com.junghoon.movie.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.junghoon.movie.MovieDetailRoute
import com.junghoon.movie.core.ui.base.ErrorState

fun NavController.navigateMovieDetail(movieId: Int) {
    navigate(MovieDetailRoute.detailRoute(movieId))
}

fun NavGraphBuilder.movieDetailNavGraph(
    onBackClick: () -> Unit,
    onShowErrorSnackBar: (ErrorState) -> Unit
) {
    composable(
        route = "${MovieDetailRoute.route}/{movieId}",
        arguments = listOf(
            navArgument("movieId") {
                type = NavType.IntType
            }
        )
    ) { navBackStackEntry ->
        val movieId = navBackStackEntry.arguments?.getInt("movieId") ?: 0
        MovieDetailRoute(
            movieId = movieId,
            onBackClick = onBackClick,
            onShowErrorSnackBar = onShowErrorSnackBar
        )
    }
}

object MovieDetailRoute {
    const val route: String = "moveDetail"

    fun detailRoute(movieId: Int): String = "$route/$movieId"
}
