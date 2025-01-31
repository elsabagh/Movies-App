package com.example.movies_app.details.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ImageNotSupported
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.movies_app.R
import com.example.movies_app.movieList.data.remote.MovieApi
import com.example.movies_app.movieList.domain.model.Movie
import com.example.movies_app.movieList.util.RatingBar
import com.example.movies_app.ui.theme.MoviesAppTheme

@Composable
fun DetailsScreen() {
    val detailsViewModel = hiltViewModel<DetailsViewModel>()
    val detailsState = detailsViewModel.detailsState.collectAsState().value

    val backDropImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + detailsState.movie?.backdrop_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    val posterImageState = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(MovieApi.IMAGE_BASE_URL + detailsState.movie?.poster_path)
            .size(Size.ORIGINAL)
            .build()
    ).state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MovieBackDropImage(
            backDropImageState = backDropImageState,
            title = detailsState.movie?.title ?: ""
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(240.dp)
            ) {
                MoviePosterImage(
                    posterImageState = posterImageState,
                    title = detailsState.movie?.title ?: ""
                )

            }
            Spacer(modifier = Modifier.width(16.dp))

            MovieDetails(detailsState = detailsState)
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = stringResource(R.string.overview),
            fontSize = 19.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            text = detailsState.movie?.overview ?: "",
            fontSize = 15.sp
        )
    }
}

@Composable
fun MovieBackDropImage(
    backDropImageState: AsyncImagePainter.State,
    title: String,
) {
    when (backDropImageState) {
        is AsyncImagePainter.State.Loading,
        is AsyncImagePainter.State.Empty,
            -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
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
                    .height(220.dp)
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
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                painter = backDropImageState.painter,
                contentDescription = title,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun MoviePosterImage(
    posterImageState: AsyncImagePainter.State,
    title: String,
) {
    when (posterImageState) {
        is AsyncImagePainter.State.Loading,
        is AsyncImagePainter.State.Empty,
            -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("Loading...", color = Color.Gray)
            }
        }

        is AsyncImagePainter.State.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp))
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
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(12.dp)),
                painter = posterImageState.painter,
                contentDescription = title,
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun MovieDetails(detailsState: DetailsState) {
    detailsState.movie?.let { movie ->
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = movie.title,
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp)
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
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(R.string.language) + movie.original_language,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(R.string.release_date) + movie.release_date,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(R.string.votes) + movie.vote_count,
                fontSize = 15.sp
            )
        }
    }
}
