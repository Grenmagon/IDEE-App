package at.IDEE.idee_app.API

import android.util.Log
import at.IDEE.idee_app.AppViewModel
import at.IDEE.idee_app.AskLawDetail
import at.IDEE.idee_app.FunFact
import at.IDEE.idee_app.LawDetail
import at.IDEE.idee_app.QuizQuestion
import at.IDEE.idee_app.LawDetailsShort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchData(
    private val appViewModel: AppViewModel
) {
    private fun api(): ApiService =
        ApiClient.createApi(appViewModel.serverAddress)
    suspend fun fetchCategories(): List<String> {
        return try {
            Log.d("fetchCategories", "Fill Test Categories")

            val response = withContext(Dispatchers.IO) {
                api().getCategories()
            }

            Log.d("fetchCategories", response.categories.toString())
            response.categories

        } catch (e: Exception) {
            Log.d("fetchCategories", "ERROR: ${e.message}")
            appViewModel.showError("ERROR: ${e.message}")
            emptyList()
        }
    }

    suspend fun fetchLawDetailsShort(category: String): LawDetailsShort {
        return try {
            Log.d("fetchLawDetailsShort", "Fill Test Categories")

            val response = withContext(Dispatchers.IO) {
                api().getShortLawDetails(category)
            }

            Log.d("fetchLawDetailsShort", response.lawDetailShort.toString())
            response

        } catch (e: Exception) {
            Log.d("fetchLawDetailsShort", "ERROR: ${e.message}")
            appViewModel.showError("ERROR: ${e.message}")
            LawDetailsShort(lawDetailShort = emptyList())
        }
    }


    suspend fun fetchRandomFunFact(): FunFact {
        return try {
            Log.d("fetchRandomFunFact", "get Funfact")

            val response = withContext(Dispatchers.IO) {
                api().getFunfact()
            }

            Log.d("fetchRandomFunFact", response.text)
            response

        } catch (e: Exception) {
            Log.d("fetchRandomFunFact", "ERROR: ${e.message}")
            appViewModel.showError("ERROR: ${e.message}")
            FunFact("No Funfact found")
        }
    }

    suspend fun fetchLawById(id: AskLawDetail): LawDetail {
        return try {
            Log.d("fetchLawById", "fetchLawById")

            val response = withContext(Dispatchers.IO) {
                api().getLawDetail(id)
            }

            Log.d("fetchCategories", response.toString())
            response

        } catch (e: Exception) {
            Log.d("fetchCategories", "ERROR: ${e.message}")
            appViewModel.showError("ERROR: ${e.message}")
            LawDetail(
                id = "",
                title = "",
                category = "",
                paragraph = "",
                summary = "",
                officialText = "",
                source = "",
                lawyer = ""
            )
        }
    }

    suspend fun fetchQuizQuestions(): List<QuizQuestion> {
        return try {
            Log.d("fetchQuizQuestions", "fetchQuizQuestions")

            val response = withContext(Dispatchers.IO) {
                api().getQuizQuestions()
            }

            Log.d("fetchQuizQuestions", response.toString())
            response.questions

        } catch (e: Exception) {
            Log.d("fetchQuizQuestions", "ERROR: ${e.message}")
            appViewModel.showError("ERROR: ${e.message}")
            emptyList<QuizQuestion>()

        }
    }
}