package at.IDEE.idee_app


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController

@Composable
fun ErrorDialog(
    errorMessage: String?,
    onDismiss: () -> Unit,
    navController: NavHostController? = null,
    navigateHome: Boolean = false,
    homeRoute: String = "home"
) {
    if (!errorMessage.isNullOrEmpty()) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Fehler") },
            text = { Text(errorMessage) },
            confirmButton = {
                Button(onClick = {
                    onDismiss()
                    if (navigateHome && navController != null) {
                        navController.navigate(homeRoute) {
                            popUpTo(homeRoute) { inclusive = true }
                        }
                    }
                }) {
                    Text("OK")
                }
            }
        )
    }
}