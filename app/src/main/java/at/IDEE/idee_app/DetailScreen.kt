package at.IDEE.idee_app

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun DetailScreen(
    navController: NavHostController,
    gesetzId: String,
    appViewModel: AppViewModel,
    //viewModel: DetailViewModel = viewModel()
) {
    val context = LocalContext.current

    val viewModel: DetailViewModel = viewModel(
        factory = DetailViewModelFactory(appViewModel, context)
    )

    LaunchedEffect(gesetzId) {
        viewModel.loadLaw(gesetzId)
    }

    val gesetz = viewModel.lawDetail.value
    //val isFavorite = viewModel.isFavorite.value
    val favorites = viewModel.favorites.collectAsState()
    val isFavorite = favorites.value.contains(FavoriteLaw(gesetz?.id ?: "0", gesetz?.title ?: ""))


    if (gesetz == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        topBar = {
            GesetzesTopBar(
                navController = navController,
                showBackButton = true,
                appViewModel = appViewModel
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                DetailHeader(
                    gesetz = gesetz,
                    //isFavorite = viewModel.isFavorite.value,
                    isFavorite = isFavorite,
                    onFavoriteToggle = {
                        //viewModel.toggleFavorite(gesetz.id)
                        viewModel.toggleFavorite(FavoriteLaw(gesetz.id, gesetz.title))
                    }
                )
            }
            item {
                TabSelector(
                    selectedTab = viewModel.ausgewaehlterTab.value,
                    onTabSelected = { viewModel.ausgewaehlterTab.value = it })
            }
            item {
                if (viewModel.ausgewaehlterTab.value == "einfach") {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        InfoCard(
                            title = "Verständlich erklärt",
                            content = gesetz.summary ,
                            icon = Icons.Default.ContentCopy,
                            appViewModel
                        )
                    }
                } else {
                    InfoCard(
                        title = "Offizieller Gesetzestext",
                        content = gesetz.officialText,
                        icon = Icons.Default.Gavel,
                        appViewModel
                    )
                }
            }
            item { SourceLink(text = gesetz.source) }
        }
    }

    // --- Globaler Fehler-Dialog ---
    ErrorDialog(
        errorMessage = appViewModel.errorMessage.value,
        onDismiss = { appViewModel.clearError() },
        navController = navController,
        navigateHome = true // optional
    )
}

/* ---------------- HELPER COMPOSABLES ---------------- */

@Composable
fun DetailHeader(
    gesetz: LawDetail,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = gesetz.category,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }

        Text(
            text = gesetz.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = gesetz.paragraph,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            Button(
                onClick = onFavoriteToggle,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFavorite)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isFavorite)
                        MaterialTheme.colorScheme.onPrimary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = null)
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text(if (isFavorite) "Favorit" else "Zu Favoriten")
            }
        }
    }
}

@Composable
fun TabSelector(
    selectedTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = listOf("Einfache Sprache", "Originaltext")
    val selectedIndex = if (selectedTab == "einfach") 0 else 1

    TabRow(selectedTabIndex = selectedIndex) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedIndex == index,
                onClick = {
                    onTabSelected(if (index == 0) "einfach" else "original")
                },
                text = { Text(text = title) }
            )
        }
    }
}

@Composable
fun InfoCard(
    title: String,
    content: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    appViewModel: AppViewModel
) {
    val catModeEnabled = appViewModel.catModeEnabled

    //Text abhängig vom Modus
    val displayText by remember(content, catModeEnabled) {
        mutableStateOf(
            if (catModeEnabled)
                appViewModel.translateToCatSpeech(content)
            else
                content
        )
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton (onClick = {  }) {
                    Icon(
                        icon,
                        contentDescription = "Kopieren"
                    )
                }
            }
            Spacer (Modifier.height(8.dp))
            Text (
                text = displayText,
                style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun SourceLink(
    text:String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            Icons.Default.Link,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Text (text, style = MaterialTheme.typography.bodySmall)
    }
}

/* ---------------- PREVIEW ---------------- */

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    IstDasEhErlaubt_v2Theme {
        DetailScreen(
            navController = rememberNavController(), appViewModel = viewModel(),
            gesetzId = "§132 StGB",
        )
    }
}
