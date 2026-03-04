package com.example.architechturestartercode.presentation.movies

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.architechturestartercode.Resource
import com.example.architechturestartercode.data.movie.MoviesRepository
import com.example.architechturestartercode.data.movie.datasource.local.MoviesLocalDataSource
import com.example.architechturestartercode.data.movie.datasource.remote.MoviesRemoteDataSource
import com.example.architechturestartercode.presentation.allmovies.ui.MoviesEvent
import com.example.architechturestartercode.presentation.allmovies.ui.MoviesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MoviesViewModel(
    private val repository: MoviesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MoviesUiState())

    val uiState: StateFlow<MoviesUiState> = _uiState

    init {
        loadMovies()
    }

    private fun loadMovies() {

        viewModelScope.launch {

            repository.getAllMovies().collect { result ->

                when (result) {

                    is Resource.Loading -> {

                        _uiState.value =
                            _uiState.value.copy(
                                isLoading = true,
                                error = null
                            )
                    }

                    is Resource.Success -> {

                        _uiState.value =
                            _uiState.value.copy(
                                movies = result.data,
                                isLoading = false
                            )
                    }

                    is Resource.Error -> {

                        _uiState.value =
                            _uiState.value.copy(
                                error = result.message,
                                isLoading = false
                            )
                    }
                }
            }
        }
    }

    fun onEvent(event: MoviesEvent) {

        when (event) {

            MoviesEvent.LoadMovies -> loadMovies()

            MoviesEvent.Refresh -> loadMovies()

            is MoviesEvent.AddToFav -> {

                viewModelScope.launch {

                    repository.insertFav(event.movie)

                }
            }
        }
    }

}

@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val local = MoviesLocalDataSource(application)
        val remote = MoviesRemoteDataSource()
        return MoviesViewModel(MoviesRepository(remote, local)) as T
    }
}

