package at.IDEE.idee_app

import android.util.Log

class LawRemoteDataSource {

    suspend fun fetchCategories(): List<String> {
        Log.d("fetchCategories", "Fill Test Categories")
        var lst = listOf<String>("Test1", "Test2", "Test3")
        return lst
        //return emptyList() // später API
    }

    suspend fun fetchRandomFunFact(): FunFact {
        return FunFact("")
    }

    suspend fun fetchLawById(id: String): LawDetail {
        Log.d("fetchLawById", "fetchLawById")
        return LawDetail(
            id = id,
            title = "",
            category = "",
            paragraph = "",
            summary = "",
            officialText = "",
            source = ""
        )
    }

    suspend fun fetchQuizQuestions(count: Int): List<QuizQuestion> {
        return emptyList()
    }
}