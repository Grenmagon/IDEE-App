package at.IDEE.idee_app

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavGraph(navController: NavHostController, appViewModel: AppViewModel) {

    NavHost(
        navController = navController,
        startDestination = "disclaimer"
    ) {

        composable("disclaimer") {
            DisclaimerScreen(
                onAccept = {
                    navController.navigate("home") {
                        popUpTo("disclaimer") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            Log.d("NavGraph", "GOTO home")
            HomeScreen(navController = navController, appViewModel = appViewModel)
        }

        composable(
            route = "details/{gesetzId}",
            arguments = listOf(
                navArgument("gesetzId") {
                    type = NavType.StringType
                    nullable = true
                }
            )
        ) { backStackEntry ->
            val gesetzId = backStackEntry.arguments?.getString("gesetzId")

            Log.d("NavGraph", "GOTO details/${gesetzId}")
            if (gesetzId != null) {
                DetailScreen(
                    navController = navController,
                    appViewModel = appViewModel,
                    gesetzId = gesetzId
                )
            }
        }

        composable("favorites") {
            Log.d("NavGraph", "GOTO favorites")
            FavoritesScreen(navController = navController, appViewModel = appViewModel)
        }

        composable("quiz") {
            QuizScreen(navController = navController, appViewModel = appViewModel)
        }

        composable("quiz_question") {
            QuizQuestionScreen(navController = navController, appViewModel = appViewModel)
        }

        composable(
            route = "quiz_result/{anzahlFragen}/{anzahlRichtig}",
            arguments = listOf(
                navArgument("anzahlFragen") { type = NavType.IntType },
                navArgument("anzahlRichtig") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val anzahlFragen = backStackEntry.arguments?.getInt("anzahlFragen") ?: 0
            val anzahlRichtig = backStackEntry.arguments?.getInt("anzahlRichtig") ?: 0

            QuizResultScreen(
                navController = navController,
                appViewModel = appViewModel,
                anzahlFragen = anzahlFragen,
                anzahlRichtig = anzahlRichtig
            )
        }

        composable("options") {
            OptionsScreen(
                navController = navController,
                appViewModel = appViewModel
            )
        }

    }
}

