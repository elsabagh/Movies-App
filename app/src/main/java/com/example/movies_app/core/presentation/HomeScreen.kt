package com.example.movies_app.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movies_app.R
import com.example.movies_app.core.presentation.components.BottomNavigationBar
import com.example.movies_app.core.presentation.navgraph.Screen
import com.example.movies_app.movieList.presentation.MovieListViewModel
import com.example.movies_app.movieList.presentation.PopularMoviesScreen
import com.example.movies_app.movieList.presentation.UpcomingMoviesScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()

    // State to track which screen is selected for the TopAppBar
    val selectedScreen = remember { mutableStateOf(Screen.PopularMovieList.route) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedScreen.value == Screen.PopularMovieList.route)
                            stringResource(R.string.popular_movies)
                        else
                            stringResource(R.string.upcoming_movies),
                        fontSize = 20.sp
                    )
                },
                modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(bottomNavController, movieListViewModel::onEvent)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.PopularMovieList.route
            ) {
                composable(Screen.PopularMovieList.route) {
                    selectedScreen.value = Screen.PopularMovieList.route
                    PopularMoviesScreen(
                        movieListState = movieListState,
                        navHostController = navController,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                composable(Screen.UpcomingMovieList.route) {
                    selectedScreen.value = Screen.UpcomingMovieList.route
                    UpcomingMoviesScreen(
                        movieListState = movieListState,
                        navHostController = navController,
                        onEvent = movieListViewModel::onEvent
                    )
                }
            }
        }
    }
}