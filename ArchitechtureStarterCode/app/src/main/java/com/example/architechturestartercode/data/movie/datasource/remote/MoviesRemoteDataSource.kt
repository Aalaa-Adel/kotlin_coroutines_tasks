package com.example.architechturestartercode.data.movie.datasource.remote

import com.example.architechturestartercode.data.movie.model.Movie
import com.example.architechturestartercode.data.movie.model.MovieResponse
import com.example.architechturestartercode.data.network.Network
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MoviesRemoteDataSource {
    private val moviesService: MoviesService = Network.moviesService

    suspend fun getAllMovies(): Result<List<Movie>> {
        return runCatching {
            val result = moviesService.getMovies().body()?.results ?: emptyList()
            result
        }
    }
}

