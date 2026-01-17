package at.IDEE.idee_app

import at.IDEE.idee_app.API.FetchData

class LawRepository(
    //private val remote: LawRemoteDataSource = LawRemoteDataSource()
   appViewModel: AppViewModel
) {
    private val remote: FetchData = FetchData(appViewModel)
    suspend fun getAllCategories(): List<String> =
        remote.fetchCategories()

    suspend fun getRandomFunFact(): String =
        remote.fetchRandomFunFact().text

    suspend fun getShortLaw(category: String): LawDetailsShort =
        remote.fetchLawDetailsShort(category)
    suspend fun getLawById(id: AskLawDetail): LawDetail =
        remote.fetchLawById(id)

    suspend fun getRandomQuizQuestions(): List<QuizQuestion> =
        remote.fetchQuizQuestions()
}
