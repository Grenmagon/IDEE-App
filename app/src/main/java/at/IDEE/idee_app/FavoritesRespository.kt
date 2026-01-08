package at.IDEE.idee_app

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.get
import kotlin.collections.minus
import kotlin.collections.plus

class FavoritesRepository(private val context: Context) {

    private val FAVORITES_KEY = stringSetPreferencesKey("favorites")
    private val gson = Gson()

    val favoritesFlow_OLD: Flow<Set<String>> =
        context.dataStore.data.map { prefs ->
            prefs[FAVORITES_KEY] ?: emptySet()
        }

    suspend fun toggleFavorite_OLD(id: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY] ?: emptySet()
            prefs[FAVORITES_KEY] =
                if (current.contains(id))
                    current - id
                else
                    current + id
        }
    }

    suspend fun toggleFavorite(law: FavoriteLaw) {
        context.dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY] ?: emptySet()
            val exists = current.any { gson.fromJson(it, FavoriteLaw::class.java).id == law.id }
            prefs[FAVORITES_KEY] = if (exists) {
                current.filter { gson.fromJson(it, FavoriteLaw::class.java).id != law.id }.toSet()
            } else {
                current + gson.toJson(law)
            }
        }
    }

    val favoritesFlow: Flow<Set<FavoriteLaw>> = context.dataStore.data.map { prefs ->
        val jsonSet = prefs[FAVORITES_KEY] ?: emptySet()
        jsonSet.map { gson.fromJson(it, FavoriteLaw::class.java) }.toSet()
    }


    suspend fun removeFavorite(id: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[FAVORITES_KEY] ?: emptySet()
            prefs[FAVORITES_KEY] = current - id
        }
    }
}