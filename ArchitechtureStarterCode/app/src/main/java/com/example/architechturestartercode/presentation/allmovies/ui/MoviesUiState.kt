package com.example.architechturestartercode.presentation.allmovies.ui

import com.example.architechturestartercode.data.movie.model.Movie

data class MoviesUiState(

    val movies: List<Movie> = emptyList(),

    val isLoading: Boolean = false,

    val error: String? = null

)