package com.junghoon.movie.feature.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.junghoon.movie.core.ui.theme.MovieTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val navigator: MainNavigator = rememberMainNavigator()

            MovieTheme(darkTheme = true) {
                MainScreen(navigator = navigator)
            }
        }
    }
}
