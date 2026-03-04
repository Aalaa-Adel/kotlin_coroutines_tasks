package com.example.architechturestartercode.presentation.allmovies.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.architechturestartercode.data.movie.model.Movie
import com.example.architechturestartercode.presentation.favmovies.ui.theme.ArchitechtureStarterCodeTheme
import com.example.architechturestartercode.presentation.movies.*

class AllMoviesActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        val viewModel: MoviesViewModel by viewModels {
            MoviesViewModelFactory(application)
        }

        enableEdgeToEdge()

        setContent {

            ArchitechtureStarterCodeTheme {

                Scaffold(

                    modifier = Modifier.fillMaxSize(),

                    topBar = {

                        TopAppBar(

                            title = {

                                Text(
                                    text = "🎬 Movies",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )

                            },

                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                        )

                    }

                ) { innerPadding ->

                    MoviesScreen(
                        viewModel = viewModel,
                        modifier = Modifier.padding(innerPadding)
                    )

                }

            }

        }

    }

}

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel,
    modifier: Modifier = Modifier
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MoviesContent(
        modifier = modifier,
        state = uiState,
        onEvent = viewModel::onEvent
    )

}

@Composable
fun MoviesContent(
    modifier: Modifier = Modifier,
    state: MoviesUiState,
    onEvent: (MoviesEvent) -> Unit
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        when {

            state.isLoading -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    CircularProgressIndicator()

                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Loading movies...")

                }

            }

            state.error != null -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(60.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = state.error,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onEvent(MoviesEvent.Refresh) }
                    ) {

                        Text("Retry")

                    }

                }

            }

            state.movies.isEmpty() -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("No movies found")

                }

            }

            else -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    items(state.movies) { movie ->

                        MovieItem(
                            movie = movie,
                            buttonLabel = "Add to Favorites"
                        ) {

                            onEvent(MoviesEvent.AddToFav(movie))

                        }

                    }

                }

            }

        }

    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MovieItem(
    movie: Movie,
    buttonLabel: String = "Favorite",
    onClick: (Movie) -> Unit
) {

    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {

            GlideImage(
                model = movie.fullPosterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 80.dp, height = 110.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {

                    Text(
                        text = movie.language.uppercase(),
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 3.dp
                        )
                    )

                }

                Button(
                    onClick = {

                        onClick(movie)

                        Toast.makeText(
                            context,
                            "${movie.title} $buttonLabel",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(buttonLabel)

                }

            }

        }

    }

}