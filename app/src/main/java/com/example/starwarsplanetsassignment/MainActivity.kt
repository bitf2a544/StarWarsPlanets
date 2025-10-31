package com.example.starwarsplanetsassignment

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.starwarsplanetsassignment.ui.theme.StarWarsPlanetsAssignmentTheme
import coil.compose.AsyncImage
import coil.request.ImageRequest

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.starwarsplanetsassignment.ui.viewmodel.PlanetViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<PlanetViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsPlanetsAssignmentTheme {
                screen(viewModel)
            }
        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun screen(viewModel: PlanetViewModel){
    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Planets",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding ->
        PlanetListingScreen(viewModel)
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetListingScreen(viewModel: PlanetViewModel) {
    val planets by viewModel.planets.collectAsState()
    val loading by viewModel.loading.collectAsState()

    // Fetch planets when the screen first loads
    LaunchedEffect(Unit) {
        viewModel.fetchPlanets()
    }

    Box(
        modifier = Modifier
            .padding(top = 70.dp, bottom = 50.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            loading -> CircularProgressIndicator()

            planets.isEmpty() -> Text("No planets found")

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(planets) { planet ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = {

                          Log.e("Clicked:", "${planet.name}")

                        },
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("https://picsum.photos/200") // 🌐 Random Picsum image
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "Planet Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(
                                    text = "Name: ${planet.name}",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "Climate: ${planet.climate}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
