package at.IDEE.idee_app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun OptionsScreen(
    navController: NavHostController,
    appViewModel: AppViewModel
) {
    var text by remember { mutableStateOf(appViewModel.serverAddress) }

    Scaffold(
        topBar = {
            GesetzesTopBar(
                navController = navController,
                title = "Einstellungen",
                showBackButton = true,
                appViewModel = appViewModel
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Server-Adresse",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("IP:Port") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("z. B. 192.168.0.10:8080") }
            )

            Button(
                onClick = {
                    appViewModel.updateServerAddress(text.trim())
                    navController.popBackStack()
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Speichern")
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
