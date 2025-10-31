package com.example.starwarsplanetsassignment

import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
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
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.compose.ui.unit.sp
import com.example.starwarsplanetsassignment.data.model.Planet

class MainActivity : FragmentActivity() {
    private val viewModel by viewModels<PlanetViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsPlanetsAssignmentTheme {
                PlanetMainScreen(viewModel)
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetMainScreen(viewModel: PlanetViewModel) {
    var selectedPlanet by remember { mutableStateOf<Planet?>(null) }
    val activity = LocalContext.current as FragmentActivity
    val FRAGMENT_CONTAINER_ID = 0x1001

    // Listen for fragment back navigation (Keep this logic)
    DisposableEffect(activity.supportFragmentManager) {
        val listener = FragmentManager.OnBackStackChangedListener {
            if (activity.supportFragmentManager.backStackEntryCount == 0) {
                selectedPlanet = null
            }
        }
        activity.supportFragmentManager.addOnBackStackChangedListener(listener)
        onDispose {
            activity.supportFragmentManager.removeOnBackStackChangedListener(listener)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Planets",
                        modifier = Modifier.height(20.dp),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp // Example: Set text size to 22 Scaled Pixels
                        )
                    )
                },
                // ✅ ADD THIS BLOCK
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Set the background color here
                    titleContentColor = MaterialTheme.colorScheme.onPrimary // Optional: set title color for contrast
                )

            )
        }
    ) { innerPadding ->
        // Use a Box to stack the List and the Fragment container
        Box(
            modifier = Modifier
                //.padding(top = 50.dp, bottom = 50.dp)
                .fillMaxSize()
        ) {
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    // Only display the fragment container when a planet is selected.
                    .alpha(if (selectedPlanet != null) 1f else 0f)
                    .zIndex(if (selectedPlanet != null) 1f else 0f),// Ensure it is on top
                // .key("fragmentContainer"), // Key ensures stability of the underlying FrameLayout
                factory = { context ->
                    // This factory runs only once on initial composition/first use.
                    FrameLayout(context).apply {
                        id = FRAGMENT_CONTAINER_ID
                        // Set a solid background to prevent seeing the list underneath
                        setBackgroundColor(android.graphics.Color.WHITE)
                    }
                },
                // The update block runs on every recomposition but doesn't recreate the view.
                update = { frameLayout ->
                    // Optional: You can put update logic here, but for fragment hosting,
                    // the fragment manager handles updates via transactions.
                }
            )


            // 2. The List (Only show when no planet is selected)
            if (selectedPlanet == null) {
                PlanetListingScreen(viewModel) { planetObj ->
                    // 3. On click: Set state and immediately start the transaction
                    selectedPlanet = planetObj

                    activity.supportFragmentManager.beginTransaction()
                        .replace(
                            FRAGMENT_CONTAINER_ID, // Use the ID of the stable FrameLayout above
                            PlanetDetailFragment.newInstance(planetObj)
                        )
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetListingScreen(viewModel: PlanetViewModel, onPlanetClick: (Planet) -> Unit) {
    val planets by viewModel.planets.collectAsState()
    val loading by viewModel.loading.collectAsState()

    // Fetch planets when the screen first loads
    LaunchedEffect(Unit) {
        if (viewModel.planets.value.isEmpty()) {
            viewModel.fetchPlanets()
        }
    }
    Box(
        modifier = Modifier
            .padding(top = 80.dp, bottom = 40.dp)
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
                            onPlanetClick(planet)
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