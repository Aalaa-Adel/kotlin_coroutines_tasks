package com.example.architechturestartercode.presentation.allmovies.ui

import com.example.architechturestartercode.data.movie.model.Movie

sealed class MoviesEvent {

    object LoadMovies : MoviesEvent()

    object Refresh : MoviesEvent()

    data class AddToFav(val movie: Movie) : MoviesEvent()

}