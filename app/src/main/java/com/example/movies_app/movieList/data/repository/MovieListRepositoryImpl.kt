package com.example.movies_app.movieList.data.repository

import com.example.movies_app.movieList.data.local.movie.MovieDatabase
import com.example.movies_app.movieList.data.mapper.toMovie
import com.example.movies_app.movieList.data.mapper.toMovieEntity
import com.example.movies_app.movieList.data.remote.MovieApi
import com.example.movies_app.movieList.domain.model.Movie
import com.example.movies_app.movieList.domain.repository.MovieListRepository
import com.example.movies_app.movieList.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDb: MovieDatabase,
) : MovieListRepository {

    override suspend fun getMoviesList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int,
    ): Flow<Resource<List<Movie>>> {
        return flow {
            emit(Resource.Loading(true))

            val localMoviesList = movieDb.movieDao.getMovieListByCategory(category)

            val shouldLoadLocalMoviesList = localMoviesList.isNotEmpty() && !forceFetchFromRemote

            if (shouldLoadLocalMoviesList) {
                emit(
                    Resource.Success(
                        data = localMoviesList.map { movieEntity ->
                            movieEntity.toMovie(category)
                        }
                    )
                )
                emit(Resource.Loading(false))
                return@flow
            }

            val movieListFromApi = try {
                movieApi.getMoviesList(category, page)
            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Resource.Error(message = "Error loading movies"))
                return@flow
            }
            val moviesEntities = movieListFromApi.results.let {
                it.map { movieDto ->
                    movieDto.toMovieEntity(category)
                }
            }

            movieDb.movieDao.upsertMovieList(moviesEntities)

            emit(Resource.Success(
                moviesEntities.map { movieEntity ->
                    movieEntity.toMovie(category)
                }
            ))
            emit(Resource.Loading(false))

        }
    }

    override suspend fun getMovieDetails(movieId: Int): Flow<Resource<Movie>> {
        return flow {
            emit(Resource.Loading(true))
            val movieEntity = movieDb.movieDao.getMovieById(movieId)

            if (movieEntity != null) {
                emit(
                    Resource.Success(
                        movieEntity.toMovie(movieEntity.category)
                    )
                )
                emit(Resource.Loading(false))
                return@flow

                emit(Resource.Error("Error loading movie"))

                emit(Resource.Loading(false))

            }
        }
    }
}