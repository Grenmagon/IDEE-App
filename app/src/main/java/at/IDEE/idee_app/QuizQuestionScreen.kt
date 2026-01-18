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

        //Anpassung FRAGE – gut lesbar in einer Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Text(
                text = question.questionText,
                modifier = Modifier.padding(20.dp),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }


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

@Preview(showBackground = true)
@Composable
fun QuizQuestionScreenPreview() {
    IstDasEhErlaubt_v2Theme {
        val dummyNavController = rememberNavController()
        QuizQuestionScreen(navController = dummyNavController, appViewModel = viewModel(), )
    }
}