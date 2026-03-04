package com.example.architechturestartercode.presentation.favmovies.presenter

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.architechturestartercode.data.movie.MoviesRepository
import com.example.architechturestartercode.data.movie.datasource.local.MoviesLocalDataSource
import com.example.architechturestartercode.data.movie.datasource.remote.MoviesRemoteDataSource
import com.example.architechturestartercode.data.movie.model.Movie
import com.example.architechturestartercode.presentation.favmovies.ui.FavEvent
import com.example.architechturestartercode.presentation.favmovies.ui.FavUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _favoriteUiState: MutableStateFlow<FavUiState> =
        MutableStateFlow(FavUiState())

    val favoriteUiState = _favoriteUiState.asStateFlow()

    init {
        getFavoriteMovies()
    }

    fun getFavoriteMovies() {
        viewModelScope.launch {
            repository.getFavMovies()
                .catch { error ->
                    _favoriteUiState.update { it.copy(error = error.message.toString()) }
                }
                .collect { data ->
                    _favoriteUiState.update {
                        it.copy(
                            error = "",
                            movies = data,
                            isLoading = false
                        )
                    }
                }
        }
    }


    //    val uiState: StateFlow<FavUiState> =
//        repository.getFavMovies()
//
//            .map {
//
//                FavUiState(
//                    movies = it
//                )
//
//            }
//
//            .stateIn(
//
//                scope = viewModelScope,
//
//                started = SharingStarted.WhileSubscribed(5000),
//
//                initialValue = FavUiState(isLoading = true)
//
//            )
    fun deleteFavMovie(movie: Movie) {
        viewModelScope.launch {
            repository.deleteFav(movie)
        }
    }


    fun onEvent(event: FavEvent) {
        when (event) {
            FavEvent.Load -> getFavoriteMovies()
            is FavEvent.DeleteMovie -> deleteFavMovie(event.movie)
            FavEvent.Refresh -> {
                getFavoriteMovies()
            }
        }
    }


}

@Suppress("UNCHECKED_CAST")
class FavViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val local = MoviesLocalDataSource(context)
        val remote = MoviesRemoteDataSource()

        val repository = MoviesRepository(remote, local)

        return FavViewModel(repository) as T

    }
}