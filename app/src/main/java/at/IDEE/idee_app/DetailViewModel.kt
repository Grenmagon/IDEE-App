package at.IDEE.idee_app

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class DetailViewModelFactory(
    private val appViewModel: AppViewModel,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(
            repository = LawRepository(appViewModel),
            favoritesRepository = FavoritesRepository(context)
        ) as T
    }
}

class DetailViewModel(
    private val repository: LawRepository,
    private val favoritesRepository: FavoritesRepository

) : ViewModel() {

    val lawDetail = mutableStateOf<LawDetail?>(null)
    //val isFavorite = mutableStateOf(false)
    val favorites = favoritesRepository.favoritesFlow
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptySet()
        )
    var ausgewaehlterTab =  mutableStateOf("einfach")

    fun loadLaw(id: String) {
        viewModelScope.launch {
            lawDetail.value = repository.getLawById(id)
        }
    }

    fun toggleFavorite(favoriteLaw: FavoriteLaw ) {
        viewModelScope.launch {
            favoritesRepository.toggleFavorite(favoriteLaw)
            //isFavorite.value = !isFavorite.value
        }
    }
}