package com.junghoon.movie.feature.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import com.junghoon.movie.core.ui.base.ErrorState
import com.junghoon.movie.core.ui.theme.surfaceDim
import com.junghoon.movie.feature.home.navigation.homeNavGraph
import com.junghoon.movie.feature.navigation.popularNavGraph
import com.junghoon.movie.navigation.movieDetailNavGraph
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch

@Composable
internal fun MainScreen(navigator: MainNavigator = rememberMainNavigator()) {
    val snackBarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()
    val localContextResource = LocalContext.current.resources
    val onShowErrorSnackBar: (errorState: ErrorState) -> Unit = { errorState ->
        coroutineScope.launch {
            snackBarHostState.showSnackbar(
                when (errorState) {
                    is ErrorState.NetworkError -> localContextResource.getString(R.string.error_message_network)
                    else -> localContextResource.getString(R.string.error_message_api)
                }
            )
        }
    }

    Scaffold(
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceDim)
            ) {
                NavHost(
                    navController = navigator.navController,
                    startDestination = navigator.startDestination,
                ) {
                    homeNavGraph(
                        padding = padding,
                        onMovieClick = { navigator.navigateMovieDetail(it) },
                        onShowErrorSnackBar = onShowErrorSnackBar
                    )

                    movieDetailNavGraph(
                        onBackClick = navigator::popBackStackIfNotHome,
                        onShowErrorSnackBar = onShowErrorSnackBar
                    )

                    popularNavGraph(
                        padding = padding,
                        onShowErrorSnackBar = onShowErrorSnackBar,
                        onMovieClick = { navigator.navigateMovieDetail(it) }
                    )
                }
            }
        },
        bottomBar = {
            MainBottomBar(
                visible = navigator.shouldShowBottomBar(),
                tabs = MainTab.values().toList().toPersistentList(),
                currentTab = navigator.currentTab,
                onTabSelected = { navigator.navigate(it) }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    )
}


@Composable
private fun MainBottomBar(
    visible: Boolean,
    tabs: PersistentList<MainTab>,
    currentTab: MainTab?,
    onTabSelected: (MainTab) -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) }
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = Color.Black,
                    shape = RectangleShape
                )
                .padding(horizontal = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            tabs.forEach { tab ->
                MainBottomBarItem(
                    tab = tab,
                    selected = tab == currentTab,
                    onClick = { onTabSelected(tab) },
                )
            }
        }
    }
}

@Composable
private fun RowScope.MainBottomBarItem(
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .selectable(
                selected = selected,
                indication = null,
                role = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val label = stringResource(id = tab.labelId)
        val tintColor = if (selected) {
            Color.White
        } else {
            MaterialTheme.colorScheme.outline
        }

        Icon(
            modifier = Modifier.size(18.dp),
            painter = painterResource(tab.iconResId),
            contentDescription = label,
            tint = tintColor
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = tintColor
        )
    }
}

@Preview
@Composable
fun MainBottomBarPreview() {
    MainBottomBar(
        visible = true,
        tabs = MainTab.values().toList().toPersistentList(),
        currentTab = null,
        onTabSelected = {  }
    )
}