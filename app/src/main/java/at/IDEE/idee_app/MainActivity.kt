package at.IDEE.idee_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IstDasEhErlaubt_v2Theme {
                val navController = rememberNavController()
                val appViewModel: AppViewModel = viewModel()
                NavGraph(navController, appViewModel)
            }
        }
    }
}