package com.example.movies_app.movieList.domain.repository

import com.example.movies_app.movieList.domain.model.Movie
import com.example.movies_app.movieList.util.Resource
import kotlinx.coroutines.flow.Flow

interface MovieListRepository {
    suspend fun getMoviesList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int,
    ): Flow<Resource<List<Movie>>>

    suspend fun getMovieDetails(
        movieId: Int,
    ): Flow<Resource<Movie>>

}