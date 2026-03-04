package com.example.architechturestartercode.presentation.favmovies.ui

import com.example.architechturestartercode.data.movie.model.Movie

sealed class FavEvent {

    object Load: FavEvent()
    object Refresh: FavEvent()
    data class DeleteMovie(val movie: Movie) : FavEvent()

}