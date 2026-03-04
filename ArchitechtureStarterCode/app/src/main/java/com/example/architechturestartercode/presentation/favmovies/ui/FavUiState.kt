package com.example.architechturestartercode.presentation.favmovies.ui

import com.example.architechturestartercode.data.movie.model.Movie

data class FavUiState(

    val movies: List<Movie> = emptyList(),

    val isLoading: Boolean = false,

    val error: String? = null

)