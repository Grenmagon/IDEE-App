package at.IDEE.idee_app

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Composable
fun QuizQuestionScreen(
    navController: NavHostController,
    appViewModel: AppViewModel
) {
    val viewModel: QuizViewModel = viewModel(
        factory = QuizViewModelFactory(appViewModel)
    )

    val questions = viewModel.questions.value
    val index = viewModel.currentIndex.value
    val score = viewModel.score.value

    LaunchedEffect(Unit) {
        if (questions.isEmpty()) {
            viewModel.loadQuiz()
        }
    }

    // Quiz beendet
    if (index >= questions.size && questions.isNotEmpty()) {
        navController.navigate(
            "quiz_result/${questions.size}/$score"
        ) {
            popUpTo("quiz_question") { inclusive = true }
        }
        return
    }

    if (questions.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    val question = questions[index]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Frage ${index + 1} von ${questions.size}",
            style = MaterialTheme.typography.labelLarge
        )

        Text(
            text = question.questionText,
            style = MaterialTheme.typography.headlineSmall
        )

        question.options.forEach { option ->
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.answer(option.isCorrect)
                }
            ) {
                Text(option.text)
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



@Composable
fun AnswerOptionCard(
    text: String,
    indexLabel: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isEvaluated: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isEvaluated && isCorrect -> Color(0xFF00796B) // Richtig
        isEvaluated && isSelected && !isCorrect -> Color(0xFFD32F2F) // Falsch ausgewählt
        else -> MaterialTheme.colorScheme.outline
    }
    val backgroundColor = when {
        isEvaluated && isCorrect -> Color(0xFFE0F2F1) // Helles Grün
        isEvaluated && isSelected && !isCorrect -> Color(0xFFFFEBEE) // Helles Rot
        else -> MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = indexLabel, fontWeight = FontWeight.Bold)
                }
                Text(text = text, style = MaterialTheme.typography.bodyLarge)
            }
            if (isEvaluated && isCorrect) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Richtige Antwort",
                    tint = borderColor
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizQuestionScreenPreview() {
    IstDasEhErlaubt_v2Theme {
        val dummyNavController = rememberNavController()
        QuizQuestionScreen(navController = dummyNavController, appViewModel = viewModel(), )
    }
}