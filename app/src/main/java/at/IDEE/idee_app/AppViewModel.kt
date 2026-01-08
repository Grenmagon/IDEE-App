package at.IDEE.idee_app

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import kotlin.random.Random

class AppViewModel(application: Application) : AndroidViewModel(application) {

    // ---------- Persistenter Zustand ----------
    // Bleibt bis App-Neustart erhalten
    private val prefs =
        application.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    var serverAddress by mutableStateOf(
        prefs.getString("server_address", "10.0.2.2:6000") ?: "10.0.2.2:6000"
    )
        private set

    // ---------- UI-State ----------
    val catModeUnlocked = mutableStateOf(false)
    //val catModeEnabled = mutableStateOf(false)
    var catModeEnabled by mutableStateOf(false)
        private set

    // -------- Globaler Fehlerzustand ----------
    val errorMessage = mutableStateOf<String?>(null)


    init {
        // Wird GENAU EINMAL beim App-Start ausgeführt
        // Ergebniss Persistent -> daweil unerwünscht
        //catModeUnlocked.value = prefs.getBoolean("cat_mode_unlocked", false)
        //catModeEnabled.value = prefs.getBoolean("cat_mode_enabled", false)
    }

    // --------- Business-Logik ---------

    fun updateServerAddress(newAddress: String) {
        serverAddress = newAddress
        prefs.edit()
            .putString("server_address", newAddress)
            .apply()
    }
    fun unlockCatMode() {
        catModeUnlocked.value = true
        prefs.edit().putBoolean("cat_mode_unlocked", true).apply()
    }

    fun toggleCatMode() {
        catModeEnabled = !catModeEnabled
    }

    fun disableCatMode() {
        catModeEnabled = false
    }

    fun translateToCatSpeech(text: String): String {
        return text.split(" ").joinToString(" ") { word ->
            val match = Regex("([A-Za-zÄÖÜäöüß]+)([^A-Za-zÄÖÜäöüß]*)").find(word)
            if (match != null) {
                val (letters, punctuation) = match.destructured
                val wordLength = letters.length

                if (wordLength < 4) {
                    "miau$punctuation"
                } else {
                    val sb = StringBuilder()
                    val base = listOf('m', 'i', 'a', 'u')
                    var remaining = wordLength

                    for ((index, c) in base.withIndex()) {
                        val remainingLetters = base.size - 1 - index
                        val maxLen = (remaining - remainingLetters).coerceAtLeast(1)
                        val len = if (index == base.size - 1) remaining else Random.nextInt(1, maxLen + 1)
                        sb.append(c.toString().repeat(len))
                        remaining -= len
                    }

                    sb.toString() + punctuation
                }
            } else {
                word
            }
        }
    }

    // Fehler setzen
    fun showError(message: String) {
        errorMessage.value = message
    }

    fun clearError() {
        errorMessage.value = null
    }
}
