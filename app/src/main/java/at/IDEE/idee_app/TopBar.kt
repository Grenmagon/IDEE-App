package at.IDEE.idee_app

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GesetzesTopBar(
    navController: NavHostController,
    title: String? = null,
    showBackButton: Boolean = false,
    appViewModel: AppViewModel
    //catModeAktiv: Boolean = false,
    //onCatModeToggle: (() -> Unit)? = null
    //onCatModeToggle: Boolean = false

) {
    val catModeUnlocked = appViewModel.catModeUnlocked.value
    val catModeEnabled = appViewModel.catModeEnabled

    Log.d("CatMode", "unlocked=$catModeUnlocked enabled=$catModeEnabled")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {


        if (showBackButton) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Zurück")
            }
        } else {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("home") {
                            launchSingleTop = true
                        }
                    }
            )
        }

        title?.let {
            Spacer(Modifier.width(12.dp))
            Text(it, style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.weight(1f))


        IconButton(onClick = { navController.navigate("home") }) {
            Icon(Icons.Default.Home, "Home")
        }

        IconButton(onClick = { navController.navigate("favorites") }) {
            Icon(Icons.Default.Favorite, "Favoriten")
        }

        //CatModeButton
        if (catModeUnlocked) {
            Box(
                modifier = if (catModeEnabled)
                    Modifier.clip(CircleShape).background(Color.Yellow.copy(alpha = 0.5f))
                else Modifier
            ) {
                IconButton(onClick = { appViewModel.toggleCatMode() }/*onCatModeToggle*/) {
                    Icon(Icons.Default.Pets, "Cat Mode")
                }
            }
        }

        IconButton(onClick = { navController.navigate("quiz") }) {
            Icon(Icons.Default.Quiz, "Quiz")
        }

        IconButton(onClick = { navController.navigate("options") }) {
            Icon(Icons.Default.Settings, "Einstellungen")
        }

    }
}
