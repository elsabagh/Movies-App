package com.example.movies_app.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movies_app.movieList.domain.repository.MovieListRepository
import com.example.movies_app.movieList.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val movieId = savedStateHandle.get<Int>("movieId")

    private val _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()

    init {
        getMovieDetails(movieId ?: -1)
    }

    private fun getMovieDetails(id: Int) {
        viewModelScope.launch {
            _detailsState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieDetails(id).collect { result ->
                when (result) {
                    is Resource.Error ->
                        _detailsState.update {
                            it.copy(
                                isLoading = false
                            )
                        }

                    is Resource.Loading ->
                        _detailsState.update {
                            it.copy(
                                isLoading = result.isLoading
                            )
                        }

                    is Resource.Success ->
                        result.data?.let { movie ->
                            _detailsState.update {
                                it.copy(movie = movie)
                            }
                        }

                }
            }
        }
    }
}
