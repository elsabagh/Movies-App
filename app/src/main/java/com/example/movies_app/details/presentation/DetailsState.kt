package com.example.movies_app.details.presentation

import com.example.movies_app.movieList.domain.model.Movie

data class DetailsState(
    val movie: Movie? = null,
    val isLoading: Boolean = false,
)
