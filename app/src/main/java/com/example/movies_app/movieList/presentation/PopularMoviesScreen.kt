package com.example.movies_app.movieList.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movies_app.movieList.presentation.componentes.MovieItem
import com.example.movies_app.movieList.util.Category

@Composable
fun PopularMoviesScreen(
    movieListState: MovieListState,
    navHostController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit,
) {
    if (movieListState.popularMovieList.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 4.dp)
        ) {
            items(movieListState.popularMovieList.size) { index ->
                MovieItem(
                    movie = movieListState.popularMovieList[index],
                    navHostController = navHostController
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (index >= movieListState.popularMovieList.size - 1 && !movieListState.isLoading) {
                    onEvent(MovieListUiEvent.Paginate(Category.POPULAR))

                }
            }
        }
    }
}
//
//
//@Preview(showBackground = true)
//@Composable
//fun PopularMoviesScreenPreview() {
//    // Mock data for movie list
//    val mockMovies = List(10) { index ->
//        Movie(
//            adult = false,
//            backdrop_path = "/sample_backdrop.jpg", // Mocked path
//            genre_ids = listOf(1, 2, 3),
//            id = 123,
//            original_language = "en",
//            original_title = "Sample Original Title",
//            overview = "This is a sample movie overview.",
//            popularity = 123.45,
//            poster_path = "/sample_poster.jpg",
//            release_date = "2023-12-25",
//            title = "Sample Movie",
//            video = false,
//            vote_average = 8.5,
//            vote_count = 100,
//            category = ""
//        )
//    }
//
//    val mockState = MovieListState(
//        popularMovieList = mockMovies,
//        isLoading = false
//    )
//
//    // Mock NavHostController
//    val navHostController = rememberNavController()
//
//    // Mock onEvent function
//    val onEvent: (MovieListUiEvent) -> Unit = {}
//
//    MoviesAppTheme {
//        PopularMoviesScreen(
//            movieListState = mockState,
//            navHostController = navHostController,
//            onEvent = onEvent
//        )
//    }
//}
