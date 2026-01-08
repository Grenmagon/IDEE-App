package at.IDEE.idee_app

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class QuizViewModelFactory(
    private val appViewModel: AppViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            return QuizViewModel(appViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class QuizViewModel(
    appViewModel: AppViewModel
) : ViewModel() {
    private val repository: LawRepository = LawRepository(appViewModel)
    val questions = mutableStateOf<List<QuizQuestion>>(emptyList())
    val currentIndex = mutableStateOf(0)
    val score = mutableStateOf(0)

    fun loadQuiz() {
        viewModelScope.launch {
            questions.value = repository.getRandomQuizQuestions()
        }
    }

    fun answer(correct: Boolean) {
        if (correct) score.value++
        currentIndex.value++
    }
}