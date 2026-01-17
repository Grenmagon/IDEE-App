package at.IDEE.idee_app

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModelFactory(
    private val appViewModel: AppViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(appViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class HomeViewModel(
    appViewModel: AppViewModel
) : ViewModel() {

    private val repository: LawRepository = LawRepository(appViewModel)
    val categories = mutableStateOf<List<String>>(emptyList())
    val shortLaws = mutableStateOf<List<LawDetailShort>>(emptyList())
    val funFact = mutableStateOf<String?>(null)

    val foundtxt = mutableStateOf<String?>(null)

    fun loadHomeData() {
        viewModelScope.launch {
            //categories.value = repository.getAllCategories()
            funFact.value = repository.getRandomFunFact()
        }
    }

    fun getShortLaw(category: String)
    {
        foundtxt.value = "";
        viewModelScope.launch {
            shortLaws.value = repository.getShortLaw(category).lawDetailShort
            if (shortLaws.value.isEmpty()) {
                Log.d("fetchLawDetailsShort", "Kein Ergebnis gefunden")
                foundtxt.value = "Kein Ergebnis gefunden!"
            }
            else
                foundtxt.value = shortLaws.value.count().toString() + " Ergebnise gefunden!"

        }
    }
}
