package com.example.movies_app.core.presentation.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movies_app.R
import com.example.movies_app.core.presentation.navgraph.Screen
import com.example.movies_app.movieList.presentation.MovieListUiEvent
import com.example.movies_app.ui.theme.MoviesAppTheme

@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit,
) {

    val items = listOf(
        BottomItem(
            title = stringResource(R.string.popular),
            icon = Icons.Rounded.Movie
        ), BottomItem(
            title = stringResource(R.string.upcoming),
            icon = Icons.Rounded.Upcoming
        )
    )

    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
        MaterialTheme.colorScheme.inverseOnSurface,
        tonalElevation = 10.dp
    ) {
        items.forEachIndexed { index, bottomItem ->
            NavigationBarItem(selected = selected.intValue == index, onClick = {
                selected.intValue = index
                when (selected.intValue) {
                    0 -> {
                        onEvent(MovieListUiEvent.Navigate)
                        bottomNavController.popBackStack()
                        bottomNavController.navigate(Screen.PopularMovieList.route)
                    }

                    1 -> {
                        onEvent(MovieListUiEvent.Navigate)
                        bottomNavController.popBackStack()
                        bottomNavController.navigate(Screen.UpcomingMovieList.route)
                    }
                }
            }, icon = {
                Icon(
                    imageVector = bottomItem.icon,
                    contentDescription = bottomItem.title,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }, label = {
                Text(
                    text = bottomItem.title, color = MaterialTheme.colorScheme.onBackground
                )
            },
            )
        }
    }
}


data class BottomItem(
    val title: String, val icon: ImageVector,
)

@Preview()
@Preview(
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun BottomNavigationBarPreview() {
    MoviesAppTheme {
        BottomNavigationBar(NavHostController(LocalContext.current), {

        }
        )
    }
}

