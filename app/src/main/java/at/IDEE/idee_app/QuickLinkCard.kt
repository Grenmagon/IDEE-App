package at.IDEE.idee_app

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier // <-- DER ENTSCHEIDENDE IMPORT
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QuickLinkCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier // <-- Hier wird Modifier als Typ verwendet
) {
    Card(
        modifier = modifier // <-- Und hier wird er verwendet
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Mehr anzeigen"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuickLinkCardPreview() {
    // Annahme: Dein Theme heißt IstDasEhErlaubt_v2Theme
    IstDasEhErlaubt_v2Theme {
        QuickLinkCard(title = "Vorschau Titel", onClick = {})
    }
}