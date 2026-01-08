package at.IDEE.idee_app

data class Categories(
    val categories : List<String>
)

data class LawDetailShort(
    val id: String,
    val title: String
)

data class LawDetailsShort(
    val lawDetailShort: List<LawDetailShort>
)
data class LawDetail(
    val id: String,
    val title: String,
    val category: String,
    val paragraph: String,
    val summary: String,
    val officialText: String,
    val source: String
)

data class FavoriteLaw(
    val id: String,
    val title: String
)

data class FunFact(
    val text: String
)

data class AnswerOption(
    val text: String,
    val isCorrect: Boolean
)

data class QuizQuestion(
    val questionText: String,
    val difficulty: String,
    val options: List<AnswerOption>,
    val explanation: String
)

data class QuizQuestions(
    val questions: List<QuizQuestion>
)
