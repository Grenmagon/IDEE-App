package at.IDEE.idee_app

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel
) {
    // ViewModel mit Factory korrekt erzeugen
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(appViewModel)
    )

    LaunchedEffect(Unit) {
        viewModel.loadHomeData()
    }

    val funFact by viewModel.funFact
    val foundtxt by viewModel.foundtxt

    var suchtext by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            GesetzesTopBar(
                navController = navController,
                showBackButton = false,
                appViewModel = appViewModel
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Wichtiger Hinweis",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "Diese App stellt keine Rechtsberatung dar.",
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                "Ist Das Eh Erlaubt",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {

                TextField(
                    value = suchtext,
                    onValueChange = { suchtext = it },
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("Suchstring") },
                    //leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        if (suchtext.isNotBlank()) {
                            viewModel.getShortLaw(suchtext)
                        }
                    })
                )
                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = {
                        if (suchtext.isNotBlank()) {
                            viewModel.getShortLaw(suchtext)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Suchen"
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            )
            {
                foundtxt?.let {
                    Text(
                        it,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            val shortLaw by viewModel.shortLaws

            LazyColumn(
                //modifier = Modifier.fillMaxSize(),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)

            )
            {
                items(shortLaw){ link ->
                    QuickLinkCard(
                        title = link.title,
                        onClick = {
                            // z. B. Navigation mit ID
                            Log.d("","")
                            navController.navigate("details/${link.dokid}")
                        }
                    )
                }

            }

            //Spacer(Modifier.weight(1f))

            funFact?.let {
                FunFactCard(
                    content = it,
                    icon = Icons.Default.Lightbulb
                )
            }
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    IstDasEhErlaubt_v2Theme {
        HomeScreen(navController = rememberNavController(), appViewModel = viewModel())
    }
}