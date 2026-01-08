package at.IDEE.idee_app.API

import at.IDEE.idee_app.API.ApiClient.API
import at.IDEE.idee_app.FunFact
import at.IDEE.idee_app.Categories
import at.IDEE.idee_app.LawDetail
import at.IDEE.idee_app.LawDetailsShort
import at.IDEE.idee_app.QuizQuestions
import retrofit2.http.*

interface ApiService {
    @GET(API + "categories.json")
    suspend fun getCategories(): Categories

    @GET(API + "funfact.json")
    suspend fun getFunfact(): FunFact

    @GET(API + "LawDetailsShort.json")
    suspend fun getShortLawDetails(@Query("category") category: String): LawDetailsShort

    @GET(API + "LawDetail.json")
    suspend fun getLawDetail(@Query("id") id: String): LawDetail

    @GET(API + "QuizQuestions.json")
    suspend fun getQuizQuestions() : QuizQuestions
}