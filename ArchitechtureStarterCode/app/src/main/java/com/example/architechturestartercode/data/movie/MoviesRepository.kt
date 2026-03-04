package com.example.architechturestartercode.data.movie

import com.example.architechturestartercode.Resource
import com.example.architechturestartercode.data.movie.datasource.local.MoviesLocalDataSource
import com.example.architechturestartercode.data.movie.datasource.remote.MoviesRemoteDataSource
import com.example.architechturestartercode.data.movie.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MoviesRepository(

    private val remote: MoviesRemoteDataSource,
    private val local: MoviesLocalDataSource

) {

    fun getAllMovies(): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading())
        val result = remote.getAllMovies()

        if (result.isSuccess) {
            emit(Resource.Success(result.getOrDefault(emptyList())))
        } else {
            emit(Resource.Error("Failed to fetch data"))
        }
    }.catch {
        emit(Resource.Error(it.message.toString()))
    }.flowOn(Dispatchers.IO)


    fun getFavMovies(): Flow<List<Movie>> {

        return local.getAllMovies()

    }

    suspend fun insertFav(movie: Movie) {

        local.insertMovie(movie)

    }

    suspend fun deleteFav(movie: Movie) {

        local.deleteMovie(movie)

    }

}