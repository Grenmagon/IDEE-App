package at.IDEE.idee_app

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun QuizResultScreen(
    navController: NavHostController,
    anzahlFragen: Int,
    anzahlRichtig: Int,
    appViewModel: AppViewModel
) {
    val context = LocalContext.current
    val alleRichtig = anzahlFragen == anzahlRichtig

    if (alleRichtig) {
        LaunchedEffect(Unit) {
            appViewModel.unlockCatMode()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (alleRichtig) {
            Icon(
                imageVector = Icons.Default.Celebration,
                contentDescription = "Erfolg",
                modifier = Modifier.size(128.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Perfekt! Alle Fragen richtig!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Du hast den Cat Mode freigeschaltet!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        } else {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fehlschlag",
                modifier = Modifier.size(128.dp),
                tint = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Fast geschafft!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Du hast $anzahlRichtig von $anzahlFragen Fragen richtig beantwortet. Versuche es erneut, um den Cat Mode freizuschalten!",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            // Zurück zum HomeScreen und alle Quiz-Seiten vom Stapel entfernen
            navController.navigate("home") {
                popUpTo("home") { inclusive = true }
            }
        }) {
            Text("Zurück zum Start")
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