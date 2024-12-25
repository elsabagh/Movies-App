package com.example.movies_app.movieList.presentation.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.movies_app.core.presentation.navgraph.Screen
import com.example.movies_app.movieList.data.remote.MovieApi
import com.example.movies_app.movieList.domain.model.Movie
import com.example.movies_app.movieList.util.RatingBar
import com.example.movies_app.movieList.util.getAverageColor
import com.example.movies_app.ui.theme.MoviesAppTheme

@Composable
fun MovieItem(
    movie: Movie,
    navHostController: NavHostController,
) {
    val imageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + movie.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val defaultColor = MaterialTheme.colorScheme.secondaryContainer
    var dominantColor by remember { mutableStateOf(defaultColor) }

    // Main Container
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .width(200.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(MaterialTheme.colorScheme.secondaryContainer, dominantColor)
                )
            )
            .clickable { navHostController.navigate(Screen.Details.route + "/${movie.id}") }
    ) {
        // Image Section
        MovieImage(
            imageState = imageState,
            onDominantColorCalculated = { color ->
                dominantColor = color
            },
            title = movie.title
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Details Section
        MovieDetails(movie = movie)
    }
}

@Composable
fun MovieImage(
    imageState: AsyncImagePainter.State,
    onDominantColorCalculated: (Color) -> Unit,
    title: String,
) {
    when (imageState) {
        is AsyncImagePainter.State.Loading,
        is AsyncImagePainter.State.Empty,
            -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading...", color = Color.Gray)
            }
        }

        is AsyncImagePainter.State.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(70.dp),
                    imageVector = Icons.Rounded.ImageNotSupported,
                    contentDescription = title
                )
            }
        }

        is AsyncImagePainter.State.Success -> {
            onDominantColorCalculated(
                getAverageColor(
                    imageBitmap = imageState.result.drawable.toBitmap().asImageBitmap()
                )
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(22.dp)),
                painter = imageState.painter,
                contentDescription = title,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun MovieDetails(movie: Movie) {
    Text(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
        text = movie.title,
        color = Color.White,
        fontSize = 15.sp,
        maxLines = 1
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, bottom = 12.dp, top = 4.dp)
    ) {
        RatingBar(
            starsModifier = Modifier.size(18.dp),
            rating = movie.vote_average / 2
        )

        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = movie.vote_average.toString().take(3),
            color = Color.LightGray,
            fontSize = 14.sp,
            maxLines = 1,
        )
    }
}


@Preview(showBackground = true)
@Composable
fun MovieItemPreview() {
    MoviesAppTheme {
        val sampleMovie = Movie(
            adult = false,
            backdrop_path = "/sample_backdrop.jpg", // Mocked path
            genre_ids = listOf(1, 2, 3),
            id = 123,
            original_language = "en",
            original_title = "Sample Original Title",
            overview = "This is a sample movie overview.",
            popularity = 123.45,
            poster_path = "/sample_poster.jpg",
            release_date = "2023-12-25",
            title = "Sample Movie",
            video = false,
            vote_average = 8.5,
            vote_count = 100,
            category = ""
        )

        // Mock NavHostController for the preview
        val mockNavController = rememberNavController()

        MovieItem(movie = sampleMovie, navHostController = mockNavController)
    }
}
